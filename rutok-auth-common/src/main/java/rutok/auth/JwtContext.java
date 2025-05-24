package rutok.auth;

import java.security.*;
import java.security.interfaces.*;
import java.security.spec.*;
import java.util.*;

import com.auth0.jwt.algorithms.*;
import lombok.*;

@RequiredArgsConstructor
public class JwtContext {

    private final JwtProperties props;

    private RSAPrivateKey privateKey;

    private RSAPublicKey publicKey;

    private Algorithm algorithm;

    private KeyFactory keyFactory;

    public Algorithm getAlgorithm() {
        if (algorithm == null) {
            algorithm = Algorithm.RSA256(getPublicKey(), getPrivateKey());
        }
        return algorithm;
    }

    private RSAPrivateKey getPrivateKey() {
        if (privateKey == null && props.getPrivateKey() != null) {
            privateKey = Optional.of(props)
                .map(JwtProperties::getPrivateKey)
                .map(this::getPrivateKeySpec)
                .map(this::generatePrivateKey)
                .orElse(null);
        }
        return privateKey;
    }

    private RSAPublicKey getPublicKey() {
        if (publicKey == null && props.getPublicKey() != null) {
            publicKey = Optional.of(props)
                .map(JwtProperties::getPublicKey)
                .map(this::getPublicKeySpec)
                .map(this::generatePublicKey)
                .orElse(null);
        }
        return publicKey;
    }

    private KeyFactory getKeyFactory() {
        if (keyFactory == null) {
            try {
                keyFactory = KeyFactory.getInstance("RSA");
            } catch (NoSuchAlgorithmException ex) {
                throw new RuntimeException("Can't instantiate KeyFactory", ex);
            }
        }
        return keyFactory;
    }

    private EncodedKeySpec getPrivateKeySpec(String keyValue) {
        var keyBytes = Base64.getDecoder().decode(keyValue);
        var keySpec = new PKCS8EncodedKeySpec(keyBytes);
        return keySpec;
    }

    private EncodedKeySpec getPublicKeySpec(String keyValue) {
        var keyBytes = Base64.getDecoder().decode(keyValue);
        var keySpec = new X509EncodedKeySpec(keyBytes);
        return keySpec;
    }

    private RSAPrivateKey generatePrivateKey(EncodedKeySpec keySpec) {
        try {
            var factory = getKeyFactory();
            return (RSAPrivateKey) factory.generatePrivate(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException("Can't generate private key", ex);
        }
    }

    private RSAPublicKey generatePublicKey(EncodedKeySpec keySpec) {
        try {
            var factory = getKeyFactory();
            return (RSAPublicKey) factory.generatePublic(keySpec);
        } catch (Exception ex) {
            throw new RuntimeException("Can't generate public key", ex);
        }
    }

}
