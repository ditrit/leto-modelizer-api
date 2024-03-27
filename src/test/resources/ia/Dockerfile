FROM php:apache
RUN a2enmod rewrite
RUN echo 'log_errors = On' >> /usr/local/etc/php/conf.d/errors.ini
RUN echo 'error_log = /dev/stderr' >> /usr/local/etc/php/conf.d/errors.ini
RUN echo '<Directory "/var/www/html">' > /etc/apache2/conf-available/rewrite.conf && \
    echo '    AllowOverride All' >> /etc/apache2/conf-available/rewrite.conf && \
    echo '</Directory>' >> /etc/apache2/conf-available/rewrite.conf && \
    a2enconf rewrite