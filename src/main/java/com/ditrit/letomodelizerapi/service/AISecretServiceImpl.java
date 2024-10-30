package com.ditrit.letomodelizerapi.service;

import com.ditrit.letomodelizerapi.config.Constants;
import com.ditrit.letomodelizerapi.model.BeanMapper;
import com.ditrit.letomodelizerapi.model.ai.AISecretRecord;
import com.ditrit.letomodelizerapi.model.error.ApiException;
import com.ditrit.letomodelizerapi.model.error.ErrorType;
import com.ditrit.letomodelizerapi.persistence.model.AIConfiguration;
import com.ditrit.letomodelizerapi.persistence.model.AISecret;
import com.ditrit.letomodelizerapi.persistence.repository.AIConfigurationRepository;
import com.ditrit.letomodelizerapi.persistence.repository.AISecretRepository;
import com.ditrit.letomodelizerapi.persistence.specification.SpecificationHelper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.hubspot.jinjava.Jinjava;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Implementation of the AccessControlService interface.
 *
 * <p>This class provides concrete implementations for the access control management operations defined in
 * AccessControlService.
 * AccessControlServiceImpl interacts with the underlying repository layer to perform these operations,
 * ensuring that business logic and data access are effectively managed.
 *
 *  <p>
 *  This service ensures that secret values are securely handled and never exposed outside of this class.
 *  Any secret retrieved by this service will have its value set to {@code null} before being returned.
 *  </p>
 *  <p>
 *  <b>Important:</b> If any operation requires the use or manipulation of a secret's value
 *  (whether encrypted or decrypted),  it must be done within this class.
 *  The secret's value is intentionally removed (set to {@code null}) before returning any {@link AISecret}
 *  instance to ensure that the value is not accessed or exposed externally.
 *  </p>
 */
@Slf4j
@Service
@Transactional
public class AISecretServiceImpl implements AISecretService {

    /**
     * The AISecretRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AISecret entities,
     * such as querying, saving, and updating access control data.
     */
    private final AISecretRepository aiSecretRepository;

    /**
     * The AIConfigurationRepository instance is injected by Spring's dependency injection mechanism.
     * This repository is used for performing database operations related to AIConfiguration entities,
     * such as querying, saving, and updating access control data.
     */
    private final AIConfigurationRepository aiConfigurationRepository;

    /**
     * The key to encrypt or decrypt secret value.
     */
    private final String secretEncryptionKey;

    /**
     * The key to encrypt or decrypt configuration.
     */
    private final String configurationEncryptionKey;

    /**
     * Size of IV.
     */
    private static final int IV_SIZE = 12;

    /**
     * Length of the Galois/Counter Mode (GCM) authentication tag.
     */
    private static final int GCM_TAG_LENGTH = 16 * 8;

    /**
     * Size of key.
     */
    private static final int KEY_SIZE = 16;
    /**
     * Constructor for AISecretServiceImpl.
     *
     * @param aiSecretRepository Repository to manage AISecret.
     * @param aiConfigurationRepository Repository to manage AIConfiguration.
     * @param secretEncryptionKey the key to encrypt or decrypt secret value.
     * @param configurationEncryptionKey the key to encrypt or decrypt configuration.
     */
    @Autowired
    public AISecretServiceImpl(final AISecretRepository aiSecretRepository,
                               final AIConfigurationRepository aiConfigurationRepository,
                               @Value("${ai.secrets.encryption.key}") final String secretEncryptionKey,
                               @Value("${ai.configuration.encryption.key}") final String configurationEncryptionKey) {
        this.aiSecretRepository = aiSecretRepository;
        this.aiConfigurationRepository = aiConfigurationRepository;
        this.secretEncryptionKey = secretEncryptionKey;
        this.configurationEncryptionKey = configurationEncryptionKey;
    }

    /**
     * Encrypts the given plain text using AES with CBC mode and PKCS5 padding.
     * This method generates a random initialization vector (IV), hashes the provided secret key using SHA-256,
     * and uses AES encryption to secure the plain text. The resulting byte array contains both the IV and the
     * encrypted text.
     *
     * @param key key to encrypt the text.
     * @param plainText the plain text to be encrypted.
     * @return a byte array containing the IV and the encrypted text.
     */
    public byte[] encrypt(final String key, final String plainText) {
        try {
            byte[] clean = plainText.getBytes();

            // Generating IV
            byte[] iv = new byte[IV_SIZE];
            SecureRandom random = new SecureRandom();
            random.nextBytes(iv);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            // Hashing key
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.update(key.getBytes(StandardCharsets.UTF_8));
            byte[] keyBytes = new byte[KEY_SIZE];
            System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            // Encrypt using AES in GCM mode
            Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec);
            byte[] encrypted = cipher.doFinal(clean);

            // Combine IV and encrypted part
            byte[] encryptedIVAndText = new byte[IV_SIZE + encrypted.length];
            System.arraycopy(iv, 0, encryptedIVAndText, 0, IV_SIZE);
            System.arraycopy(encrypted, 0, encryptedIVAndText, IV_SIZE, encrypted.length);

            return encryptedIVAndText;
        } catch (Exception e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "encryption", "failed to encrypt");
        }
    }

    /**
     * Decrypts the given byte array that contains both the initialization vector (IV) and the encrypted text.
     * This method extracts the IV, hashes the provided secret key using SHA-256, and uses AES decryption
     * to convert the encrypted bytes back into plain text.
     *
     * @param key key to decrypt the text.
     * @param encryptedIvTextBytes a byte array containing the IV and the encrypted text.
     * @return the decrypted plain text.
     */
    public String decrypt(final String key, final byte[] encryptedIvTextBytes) {
        try {
            // Extract IV
            byte[] iv = new byte[IV_SIZE];
            System.arraycopy(encryptedIvTextBytes, 0, iv, 0, iv.length);
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);

            // Extract encrypted part
            int encryptedSize = encryptedIvTextBytes.length - IV_SIZE;
            byte[] encryptedBytes = new byte[encryptedSize];
            System.arraycopy(encryptedIvTextBytes, IV_SIZE, encryptedBytes, 0, encryptedSize);

            // Hash key
            byte[] keyBytes = new byte[KEY_SIZE];
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(key.getBytes(StandardCharsets.UTF_8));
            System.arraycopy(md.digest(), 0, keyBytes, 0, keyBytes.length);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, "AES");

            // Decrypt using AES in GCM mode
            Cipher cipherDecrypt = Cipher.getInstance("AES/GCM/NoPadding");
            cipherDecrypt.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec);
            byte[] decrypted = cipherDecrypt.doFinal(encryptedBytes);

            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new ApiException(e, ErrorType.INTERNAL_ERROR, "encryption", "failed to decrypt");
        }
    }

    /**
     * Finds and returns a page of AISecret entities, filtered by provided criteria.
     * This method return data with encoded secret value.
     * Ensure the secret value is not exposed, even if it is encrypted.
     *
     * @param filters  a Map of strings representing the filtering criteria.
     * @param pageable a Pageable object for pagination information.
     * @return a Page of AISecret entities matching the specified type and filters.
     */
    private Page<AISecret> findAllWithSecretValue(final Map<String, String> filters,
                                                             final Pageable pageable) {
        return aiSecretRepository.findAll(new SpecificationHelper<>(AISecret.class, filters),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        pageable.getSortOr(Sort.by(Sort.Direction.ASC, "key"))
                )
        );
    }

    @Override
    public Page<AISecret> findAll(final Map<String, String> filters, final Pageable pageable) {
        return findAllWithSecretValue(filters, pageable)
                .map(new BeanMapper<>(AISecret.class, Constants.DEFAULT_AI_SECRET_VALUE_PROPERTY));
    }

    /**
     * Finds and returns an AISecret entity of a specific type by its ID.
     * This method return data with encoded secret value.
     * Ensure the secret value is not exposed, even if it is encrypted.
     *
     * @param id   the ID of the AISecret entity.
     * @return the found AISecret entity, or null if no entity is found with the given ID.
     */
    private AISecret findByIdWithSecretValue(final UUID id) {
        return aiSecretRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorType.ENTITY_NOT_FOUND, "id", id.toString()));
    }

    @Override
    public AISecret findById(final UUID id) {
        var aiSecret = findByIdWithSecretValue(id);

        // Ensure the secret value is not exposed, even if it is encrypted.
        // The secret value is set to null to prevent access outside this class.
        return new BeanMapper<>(AISecret.class, Constants.DEFAULT_AI_SECRET_VALUE_PROPERTY)
                .apply(aiSecret);
    }

    @Override
    public AISecret create(final AISecretRecord aiSecretRecord) {
        if (aiSecretRepository.existsByKey(aiSecretRecord.key())) {
            throw new ApiException(ErrorType.ENTITY_ALREADY_EXISTS, "key", aiSecretRecord.key());
        }

        var aiSecret = new AISecret();
        aiSecret.setKey(aiSecretRecord.key());
        aiSecret.setValue(encrypt(secretEncryptionKey, aiSecretRecord.value()));

        aiSecret = aiSecretRepository.save(aiSecret);

        // Ensure the secret value is not exposed, even if it is encrypted.
        // The secret value is set to null to prevent access outside this class.
        return new BeanMapper<>(AISecret.class, Constants.DEFAULT_AI_SECRET_VALUE_PROPERTY)
                .apply(aiSecret);
    }

    @Override
    public AISecret update(final UUID id, final AISecretRecord aiSecretRecord) {
        AISecret aiSecret = findById(id);

        aiSecret.setValue(encrypt(secretEncryptionKey, aiSecretRecord.value()));

        aiSecret = aiSecretRepository.save(aiSecret);

        // Ensure the secret value is not exposed, even if it is encrypted.
        // The secret value is set to null to prevent access outside this class.
        return new BeanMapper<>(AISecret.class, Constants.DEFAULT_AI_SECRET_VALUE_PROPERTY)
                .apply(aiSecret);
    }

    @Override
    public void delete(final UUID id) {
        AISecret aiSecret = findById(id);

        aiSecretRepository.delete(aiSecret);
    }

    @Override
    public byte[] generateConfiguration() {
        return generateConfiguration(aiConfigurationRepository.findAll());
    }

    /**
     * Generates an encrypted configuration file using a list of AI configurations.
     * <p>
     * This method uses the Jinjava templating engine to process configuration values and replace
     * placeholders with decrypted secret values. It collects all secrets from the {@code aiSecretRepository},
     * decrypts them, and stores them in a context map. Then, it iterates over the provided configurations,
     * applies the secret replacements, and constructs a JSON object containing the processed configuration values.
     * Finally, the generated configuration is encrypted before being returned as a byte array.
     *
     * @param configurations a list of {@link AIConfiguration} objects that define the keys, values, and optional
     *                       handlers for the configuration. The values may contain placeholders for secrets that will
     *                       be replaced using Jinjava.
     * @return a byte array representing the encrypted configuration, ready to be sent to the AI proxy.
     * @throws ApiException if encryption fails or any other unexpected error occurs during processing.
     */
    public byte[] generateConfiguration(final List<AIConfiguration> configurations) {
        var jinjava = new Jinjava();
        var secrets = new HashMap<String, String>();
        var context = new HashMap<String, Object>();
        var json = JsonNodeFactory.instance.objectNode();

        aiSecretRepository.findAll().forEach(secret -> secrets.put(secret.getKey(),
                decrypt(secretEncryptionKey, secret.getValue())));

        context.put("secrets", secrets);

        configurations.forEach(configuration -> {
            String key = null;

            if (configuration.getHandler() == null) {
                key = configuration.getKey();
            } else {
                key = String.format("%s.%s", configuration.getHandler(), configuration.getKey());
            }

            if (configuration.getValue() == null) {
                json.put(key, "");
            } else {
                json.put(key, jinjava.render(configuration.getValue(), context));
            }
        });

        return encrypt(configurationEncryptionKey, json.toString());
    }
}
