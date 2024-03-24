package cl.proyecto.oauth.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;

import java.util.Arrays;
import java.util.Base64;

/**
 * Servidor de autorizacion. Se firma el token con llave privada.
 */
@RefreshScope //Para quee funcione con actuator y refrescar en tiempo real las configuraciones
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private InfoAdicionalToken infoAdicionalToken;

    @Value("${config.security.oauth.client.id}")
    private String client_id;
    @Value("${config.security.oauth.client.secret}")
    private String client_secret;
    @Value("${config.security.oauth.jwt.key}")
    private String jwt_key;

    /**
     * Que permisos tendran el endpoint para obtener el token 'oauth/token mediante post',
     * en este caso cualquier cliente puede acceder a la ruta para obtener un token.
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    /**
     * Registramos a nuestros clientes (aplicaciones frontend) con sus credenciales (client_id y secret).
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient(client_id)
                .secret(passwordEncoder.encode(client_secret))//se codifica la password
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")//metodo por el cual se va a obtener el token, en este caso desde un formulario de login, se necesita el user y pass ademas del cliente_id y secret, vienen en la cabecera del request.
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600)

                .and()// segundo cliente

                .withClient("androidApp")
                .secret(passwordEncoder.encode("123456"))
                .scopes("read", "write")
                .authorizedGrantTypes("password", "refresh_token")
                .accessTokenValiditySeconds(3600)
                .refreshTokenValiditySeconds(3600);
    }

    /**
     * Configuracion para el endpoint de oauth/token mediante post, recibe authenticationManager con credenciales del usuario
     * si client id es correcto y todo sale ok se genera el token de acceso.
     * Ademas se agregan datos extras al token mediante TokenEnhancerChain obteniendo datos desde la clase InfoAdicionalToken
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //Agregamos los datos extras del token a un chain
        TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain.setTokenEnhancers(Arrays.asList(infoAdicionalToken, tokenConverter()));

        endpoints.authenticationManager(this.authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(tokenConverter())
                .tokenEnhancer(tokenEnhancerChain);//seteamos los datos extras del token
    }

    @Bean
    public JwtTokenStore tokenStore() {
        return new JwtTokenStore(tokenConverter());
    }

    @Bean
    public JwtAccessTokenConverter tokenConverter() {
        JwtAccessTokenConverter jwtAccessTokenConverter = new JwtAccessTokenConverter();
        jwtAccessTokenConverter.setSigningKey(Base64.getEncoder().encodeToString(jwt_key.getBytes()));
        return jwtAccessTokenConverter;
    }
}
