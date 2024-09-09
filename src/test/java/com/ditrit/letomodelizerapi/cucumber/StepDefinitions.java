package com.ditrit.letomodelizerapi.cucumber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.Invocation;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class StepDefinitions {
    private static final Logger LOGGER = LoggerFactory.getLogger(StepDefinitions.class);
    private static final HashMap<String, String> globalContext;
    private static final SSLContext sslcontext;

    private static final String baseURI = "https://localhost:8443/api";


    static {
        globalContext = new HashMap<>();
        globalContext.put("LIBRARY_HOST", "libraries");
        try {
            sslcontext = SSLContext.getInstance("TLS");

            sslcontext.init(null, new TrustManager[]{new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
                public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {}
                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
            }}, new java.security.SecureRandom());
        } catch (NoSuchAlgorithmException | KeyManagementException e) {
            throw new RuntimeException(e);
        }
    }

    private final Client client = ClientBuilder.newBuilder()
            .sslContext(sslcontext)
            .hostnameVerifier((s1, s2) -> true)
            .build();
    private final ObjectMapper mapper = new ObjectMapper();
    private int statusCode;
    private String body;
    private JsonNode json;
    private JsonNode resources;
    private JsonNode responseObject;

    @Given("I initialize the admin user")
    public void initAdmin() {
        String url = "jdbc:postgresql://localhost:26257/leto_db";
        String user = "leto_admin";
        String password = "password";

        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            long now = new Date().getTime();

            String sessionSQL = String.format(
                    "INSERT INTO spring_session(primary_id, session_id, creation_time, last_access_time, max_inactive_interval, expiry_time, principal_name) VALUES('%s', '%s', %d, %d, 3600, %d, %s);",
                    "6fabca49-887e-4b69-ba25-72ca90218024",
                    "6cb7cde0-0c3b-4304-acab-aa58722a8e78",
                    now,
                    now,
                    now + 3600,
                    "6179956"
            );

            String sessionAttributeSQL = String.format(
                    "INSERT INTO spring_session_attributes(session_primary_id, attribute_name, attribute_bytes) VALUES %s,%s;",
                    "('6fabca49-887e-4b69-ba25-72ca90218024', 'login', decode('ACED000574000561646D696E', 'hex'))",
                    "('6fabca49-887e-4b69-ba25-72ca90218024', 'SPRING_SECURITY_CONTEXT', decode('ACED00057372003D6F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E636F6E746578742E5365637572697479436F6E74657874496D706C000000000000026C0200014C000E61757468656E7469636174696F6E7400324C6F72672F737072696E676672616D65776F726B2F73656375726974792F636F72652F41757468656E7469636174696F6E3B7870737200536F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636C69656E742E61757468656E7469636174696F6E2E4F417574683241757468656E7469636174696F6E546F6B656E000000000000026C0200024C001E617574686F72697A6564436C69656E74526567697374726174696F6E49647400124C6A6176612F6C616E672F537472696E673B4C00097072696E636970616C74003A4C6F72672F737072696E676672616D65776F726B2F73656375726974792F6F61757468322F636F72652F757365722F4F4175746832557365723B787200476F72672E737072696E676672616D65776F726B2E73656375726974792E61757468656E7469636174696F6E2E416273747261637441757468656E7469636174696F6E546F6B656ED3AA287E6E47640E0200035A000D61757468656E746963617465644C000B617574686F7269746965737400164C6A6176612F7574696C2F436F6C6C656374696F6E3B4C000764657461696C737400124C6A6176612F6C616E672F4F626A6563743B787001737200266A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654C697374FC0F2531B5EC8E100200014C00046C6973747400104C6A6176612F7574696C2F4C6973743B7872002C6A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65436F6C6C656374696F6E19420080CB5EF71E0200014C00016371007E00077870737200136A6176612E7574696C2E41727261794C6973747881D21D99C7619D03000149000473697A65787000000002770400000002737200416F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636F72652E757365722E4F417574683255736572417574686F72697479000000000000026C0200024C000A6174747269627574657374000F4C6A6176612F7574696C2F4D61703B4C0009617574686F7269747971007E00047870737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C654D6170F1A5A8FE74F507420200014C00016D71007E00117870737200176A6176612E7574696C2E4C696E6B6564486173684D617034C04E5C106CC0FB0200015A000B6163636573734F72646572787200116A6176612E7574696C2E486173684D61700507DAC1C31660D103000246000A6C6F6164466163746F724900097468726573686F6C6478703F40000000000030770800000040000000207400056C6F67696E74000A5A6F72696E39353637307400026964737200116A6176612E6C616E672E496E746567657212E2A0A4F781873802000149000576616C7565787200106A6176612E6C616E672E4E756D62657286AC951D0B94E08B0200007870005E4C747400076E6F64655F69647400144D44513656584E6C636A59784E7A6B354E54593D74000A6176617461725F75726C74003368747470733A2F2F617661746172732E67697468756275736572636F6E74656E742E636F6D2F752F363137393935363F763D3474000B67726176617461725F696474000074000375726C74002768747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E393536373074000868746D6C5F75726C74001D68747470733A2F2F6769746875622E636F6D2F5A6F72696E393536373074000D666F6C6C6F776572735F75726C74003168747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F666F6C6C6F7765727374000D666F6C6C6F77696E675F75726C74003E68747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F666F6C6C6F77696E677B2F6F746865725F757365727D74000967697374735F75726C74003768747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F67697374737B2F676973745F69647D74000B737461727265645F75726C74003E68747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F737461727265647B2F6F776E65727D7B2F7265706F7D740011737562736372697074696F6E735F75726C74003568747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F737562736372697074696F6E737400116F7267616E697A6174696F6E735F75726C74002C68747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F6F7267737400097265706F735F75726C74002D68747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F7265706F7374000A6576656E74735F75726C74003868747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F6576656E74737B2F707269766163797D74001372656365697665645F6576656E74735F75726C74003768747470733A2F2F6170692E6769746875622E636F6D2F75736572732F5A6F72696E39353637302F72656365697665645F6576656E7473740004747970657400045573657274000A736974655F61646D696E737200116A6176612E6C616E672E426F6F6C65616ECD207280D59CFAEE0200015A000576616C75657870007400046E616D6574000F56696E63656E74204D6F6974746965740007636F6D70616E797400084064697472697420740004626C6F6771007E00237400086C6F636174696F6E7400064672616E6365740005656D61696C7400196D6F69747469652E76696E63656E7440676D61696C2E636F6D7400086869726561626C657074000362696F70740010747769747465725F757365726E616D657074000C7075626C69635F7265706F737371007E001B0000002274000C7075626C69635F67697374737371007E001B00000000740009666F6C6C6F776572737371007E001B00000006740009666F6C6C6F77696E677371007E001B0000000274000A637265617465645F6174740014323031332D31322D31335431363A35343A33325A74000A757064617465645F6174740014323032342D30312D31365431323A32333A32305A780074000B4F41555448325F55534552737200426F72672E737072696E676672616D65776F726B2E73656375726974792E636F72652E617574686F726974792E53696D706C654772616E746564417574686F72697479000000000000026C0200014C0004726F6C6571007E0004787074001053434F50455F757365723A656D61696C7871007E000F737200486F72672E737072696E676672616D65776F726B2E73656375726974792E7765622E61757468656E7469636174696F6E2E57656241757468656E7469636174696F6E44657461696C73000000000000026C0200024C000D72656D6F74654164647265737371007E00044C000973657373696F6E496471007E0004787074000F303A303A303A303A303A303A303A3174002436343432666465662D353333322D343465392D386163392D3332396265343831663164387400066769746875627372003F6F72672E737072696E676672616D65776F726B2E73656375726974792E6F61757468322E636F72652E757365722E44656661756C744F417574683255736572000000000000026C0200034C000A6174747269627574657371007E00114C000B617574686F72697469657374000F4C6A6176612F7574696C2F5365743B4C00106E616D654174747269627574654B657971007E000478707371007E00137371007E00153F400000000000307708000000400000002071007E001871007E001971007E001A71007E001D71007E001E71007E001F71007E002071007E002171007E002271007E002371007E002471007E002571007E002671007E002771007E002871007E002971007E002A71007E002B71007E002C71007E002D71007E002E71007E002F71007E003071007E003171007E003271007E003371007E003471007E003571007E003671007E003771007E003871007E003971007E003A71007E003B71007E003C71007E003E71007E003F71007E004071007E004171007E004271007E004371007E002371007E004471007E004571007E004671007E004771007E00487071007E00497071007E004A7071007E004B71007E004C71007E004D71007E004E71007E004F71007E005071007E005171007E005271007E005371007E005471007E005571007E00567800737200256A6176612E7574696C2E436F6C6C656374696F6E7324556E6D6F6469666961626C65536574801D92D18F9B80550200007871007E000C737200176A6176612E7574696C2E4C696E6B656448617368536574D86CD75A95DD2A1E020000787200116A6176612E7574696C2E48617368536574BA44859596B8B7340300007870770C000000103F4000000000000271007E001271007E00597871007E001A', 'hex'))"
            );

            stmt.executeUpdate("DELETE FROM users WHERE login = 'admin';");
            stmt.executeUpdate("DELETE FROM spring_session WHERE primary_id='6fabca49-887e-4b69-ba25-72ca90218024';");
            stmt.executeUpdate("DELETE FROM spring_session_attributes WHERE session_primary_id='6fabca49-887e-4b69-ba25-72ca90218024';");
            stmt.executeUpdate(sessionSQL);
            stmt.executeUpdate(sessionAttributeSQL);
            stmt.executeUpdate("INSERT INTO users(login) VALUES('admin')");
            stmt.executeUpdate("INSERT INTO users_access_controls(usr_id, aco_id) SELECT usr_id, (SELECT aco_id FROM access_controls WHERE name = 'SUPER_ADMINISTRATOR') FROM users where login = 'admin';");

            stmt.close();
            conn.close();
            globalContext.put("COOKIE_SESSION", "NmNiN2NkZTAtMGMzYi00MzA0LWFjYWItYWE1ODcyMmE4ZTc4");
            request("/csrf");
            globalContext.put("CSRF_HEADER", json.get("headerName").asText());
            globalContext.put("CSRF_TOKEN", json.get("token").asText());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Then("I set to context {string} with {string}")
    public void setToContext(String key, String value) {
        globalContext.put(key, value);
    }

    @When("I request {string}")
    public void request(String endpoint) {
        this.request(endpoint, "GET");
    }

    @When("I request {string} with query parameters")
    public void request(String endpoint, DataTable parameters) throws URISyntaxException, IOException, InterruptedException {
        this.request(endpoint, "GET", parameters);
    }

    @When("I request {string} with method {string} with query parameters")
    public void request(String endpoint, String method, DataTable parameters) {
        String queryParameters = parameters
                .asMaps()
                .stream()
                .map((map) -> {
                    String value = replaceWithContext(map.getOrDefault("value", ""));
                    if (value == null) {
                        value = "";
                    }
                    return String.format("%s=%s", map.get("key"), URLEncoder.encode(value, StandardCharsets.UTF_8));
                })
                .collect(Collectors.joining("&"));
        this.request(String.format("%s?%s", endpoint, queryParameters), method);
    }

    @When("I request {string} with method {string}")
    public void request(String endpoint, String method) {
        this.requestFull(endpoint, method, null, null);
    }

    @When("I request {string} with method {string} with body")
    public void requestWithTable(String endpoint, String method, DataTable table) {
        this.requestFull(endpoint, method, createBody(table), MediaType.APPLICATION_JSON);
    }

    @When("I request {string} with method {string} with json")
    public void requestWithJson(String endpoint, String method, DataTable table) {
        List<Map<String, String>> list = table.asMaps();
        ObjectNode json = JsonNodeFactory.instance.objectNode();
        list.forEach((map) -> {
            String key = map.get("key");
            String value = replaceWithContext(map.get("value"));
            String type = map.get("type");

            if ("integer".equals(type)) {
                json.put(key, Integer.parseInt(value));
            } else if ("float".equals(type)) {
                json.put(key, Float.parseFloat(value));
            } else if ("boolean".equals(type)) {
                json.put(key, Boolean.parseBoolean(value));
            } else if ("array".equals(type) || "object".equals(type)) {
                try {
                    json.set(key, mapper.readTree(value));
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            } else {
                json.put(key, value);
            }
        });

        LOGGER.info(json.toString());

        this.requestFull(endpoint, method, Entity.json(json.toString()), MediaType.APPLICATION_JSON);
    }

    public void requestFull(String endpoint, String method, Entity<?> body, String contentType) {
        String uri = baseURI + replaceWithContext(endpoint);

        WebTarget target = this.client.target(uri);
        Invocation.Builder builder = target.request();
        builder.cookie("SESSION", globalContext.get("COOKIE_SESSION"));

        if (contentType != null) {
            builder.header("Content-Type", contentType);
        }

        if (globalContext.containsKey("CSRF_HEADER")) {
            builder.header(globalContext.get("CSRF_HEADER"), globalContext.get("CSRF_TOKEN"));
        }

        LOGGER.info("{} request to {}", method, uri);
        if (body == null) {
            LOGGER.info("With no body");
        } else {
            LOGGER.info("With body: {}", body.toString());
        }
        Response response;
        if (body != null) {
            response = builder.build(method, body).invoke();
        } else {
            response = builder.build(method).invoke();
        }

        statusCode = response.getStatus();
        LOGGER.info("Receive {} as status code", statusCode);
        LOGGER.info("Receive {} as content type", response.getMediaType());
        if (statusCode != 204 && MediaType.valueOf(MediaType.APPLICATION_JSON).isCompatible(response.getMediaType())) {
            try {
                json = mapper.readTree(response.readEntity(String.class));
                LOGGER.info("With body: {}", json);
            } catch (IOException e) {
                LOGGER.error("Can't read body", e);
            }
        } else {
            this.body = response.readEntity(String.class);
        }
    }

    public String replaceWithContext(String text) {
        StrSubstitutor formatter = new StrSubstitutor(globalContext, "[", "]");
        return formatter.replace(text);
    }

    public String getToken() {
        String token = String.format("%s:%s", globalContext.get("user"), globalContext.get("password"));
        return String.format("%s %s", globalContext.get("authenticationType"), Base64.getUrlEncoder().encodeToString(token.getBytes()));
    }

    public Entity<String> createBody(DataTable table) {
        if (table == null || table.isEmpty()) {
            return null;
        }
        String value = replaceWithContext(table.cell(1, 0));
        LOGGER.info(value);

        if ("NULL".equals(value)) {
            return null;
        } else if ("EMPTY".equals(value)) {
            value = "";
        } else if ("BLANK".equals(value)) {
            value = " ";
        }

        String type = table.cell(1, 1);

        if ("BASE64".equals(type)) {
            String base64Value = Base64.getUrlEncoder().encodeToString(value.getBytes());
            return Entity.text(base64Value);
        }

        return Entity.text(value);
    }

    @Then("I expect \"{int}\" as status code")
    public void expectStatusCode(int statusCode) {
        assertEquals(statusCode, this.statusCode);
    }

    @Then("I extract resources from response")
    public void extractResponseResource() {
        this.resources = json.get("content");
    }

    @Then("I extract first resource from response")
    public void extractResponseFirstResource() {
        this.json = json.get("content").get(0);
    }

    @Then("I extract object {string} from response")
    public void extractResponseObject(String name) {
        this.responseObject = json.get(name);
    }

    @Then("I expect object field {string} is {string}")
    public void expectObjectContains(String field, String text) {
        this.expectObjectContains(field, text, "string");
    }

    @Then("I expect object field {string} is {string} as {string}")
    public void expectObjectContains(String field, String text, String type) {
        String value = replaceWithContext(text);
        assertTrue(this.checkValue(responseObject, field, value, type));
    }


    @Then("I expect response resources length is \"{int}\"")
    public void expectResourcesLengthIs(int length) {
        assertEquals(resources.size(), length);
    }

    @Then("I expect one resource contains {string} equals to {string}")
    public void expectOneResourceContains(String field, String text) {
        this.expectOneResourceContains(field, text, "string");
    }

    @Then("I expect one resource contains {string} equals to {string} as {string}")
    public void expectOneResourceContains(String field, String text, String type) {
        boolean check = false;
        String value = replaceWithContext(text);
        for (int index = 0; index < resources.size(); index += 1) {
            JsonNode resource = resources.get(index);
            check = this.checkValue(resource, field, value, type);
            if (check) {
                break;
            }
        }
        assertTrue(check);
    }

    public boolean checkValue(JsonNode resource, String field, String value, String type) {
        if ("NULL".equals(value)) {
            return resource.get(field).isNull();
        } else if ("NOT_NULL".equals(value)) {
            return !resource.get(field).isNull();
        } else if ("EMPTY".equals(value)) {
            return resource.get(field).isEmpty();
        } else if ("integer".equals(type)) {
            return resource.get(field).asInt() == Integer.parseInt(value);
        } else if ("float".equals(type)) {
            return resource.get(field).asDouble() == Double.parseDouble(value);
        } else if ("boolean".equals(type)) {
            return resource.get(field).asBoolean() == Boolean.parseBoolean(value);
        } else if ("array".equals(type) || "object".equals(type)) {
            return resource.get(field).toString().equals(value);
        }
        return resource.get(field).asText().equals(value);
    }

    @Then("I expect response resources is {string}")
    public void expectResponseTypeIs(String type) {
        if ("array".equals(type)) {
            assertTrue(resources.isArray());
        } else if ("empty".equals(type)) {
            assertTrue(resources.isEmpty());
        } else {
            assertTrue(resources.isObject());
        }
    }

    @Then("I expect response is {string}")
    public void expectResponseIsEqualsTo(String value) {
        assertEquals(json.toString(), value);
    }

    @Then("I expect body is {string}")
    public void expectBodyIsEqualsTo(String value) {
        assertEquals(this.body, value);
    }

    @Then("I expect response resources value is {string}")
    public void expectResponseIs(String value) {
        assertEquals(resources.toString(), value);
    }

    @Then("I expect response fields length is \"{int}\"")
    public void expectFieldsLengthIs(int length) {
        assertEquals(json.size(), length);
    }

    @Then("I expect object fields length is \"{int}\"")
    public void expectObjectFieldsLengthIs(int length) {

        assertEquals(responseObject.size(), length);
    }

    @Then("I expect response field {string} is {string}")
    public void expectFieldIsEqualTo(String field, String expected) {
        this.expectFieldIsEqualTo(field, expected, "string");
    }

    @Then("I expect response field {string} is {string} as {string}")
    public void expectFieldIsEqualTo(String field, String expected, String type) {
        if ("NOT_NULL".equals(expected)) {
            assertFalse(json.get(field).isNull());
            return;
        } else if ("NULL".equals(expected)) {
            assertTrue(json.get(field).isNull());
            return;
        }

        assertTrue(checkValue(json, field, replaceWithContext(expected), type));
    }

    @And("I set response field {string} to context {string}")
    public void setResponseFieldToContext(String from, String to) {
        globalContext.put(to, json.get(from).asText());
    }

    @Given("I clean role {string}")
    public void cleanRole(String name) throws URISyntaxException, IOException, InterruptedException {
        this.clean("roles", String.format("name=%s", name));
    }

    @Given("I clean group {string}")
    public void cleanGroup(String name) throws URISyntaxException, IOException, InterruptedException {
        this.clean("groups", String.format("name=%s", name));
    }

    @Given("I clean scope {string}")
    public void cleanScope(String name) throws URISyntaxException, IOException, InterruptedException {
        this.clean("scopes", String.format("name=%s", name));
    }

    @Given("I clean library {string}")
    public void cleanLibrary(String url) throws URISyntaxException, IOException, InterruptedException {
        this.clean("libraries", String.format("url=%s", url));
    }

    @And("I clean the AI conversation {string}")
    public void cleanAiConversation(String key) throws URISyntaxException, IOException, InterruptedException  {
        this.clean("ai/conversations", String.format("key=%s", key));
    }

    public void clean(String entity, String query) throws URISyntaxException, IOException, InterruptedException {
        this.request(String.format("/%s?%s", entity, query));
        if (statusCode == 200 && json.get("totalElements").asInt() > 0) {
            this.extractResponseResource();
            resources.forEach((resource) -> {
                this.request(String.format("/%s/%s", entity, resource.get("id").asText()), "DELETE");
            });
        }
    }
}
