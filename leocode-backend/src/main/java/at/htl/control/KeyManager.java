package at.htl.control;

import at.htl.entity.JwtPayload;
import io.jsonwebtoken.Jwts;
import org.testcontainers.shaded.org.bouncycastle.asn1.pkcs.PrivateKeyInfo;
import org.testcontainers.shaded.org.bouncycastle.openssl.PEMKeyPair;
import org.testcontainers.shaded.org.bouncycastle.openssl.PEMParser;
import org.testcontainers.shaded.org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;

import javax.json.bind.Jsonb;
import javax.json.bind.JsonbBuilder;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class KeyManager {
    public static PrivateKey getPrivateKeyFromPEM(Reader reader) throws IOException {

        PrivateKey key;

        try (PEMParser pem = new PEMParser(reader)) {
            JcaPEMKeyConverter jcaPEMKeyConverter = new JcaPEMKeyConverter();
            Object pemContent = pem.readObject();
            if (pemContent instanceof PEMKeyPair) {
                PEMKeyPair pemKeyPair = (PEMKeyPair) pemContent;
                KeyPair keyPair = jcaPEMKeyConverter.getKeyPair(pemKeyPair);
                key = keyPair.getPrivate();
            } else if (pemContent instanceof PrivateKeyInfo) {
                PrivateKeyInfo privateKeyInfo = (PrivateKeyInfo) pemContent;
                key = jcaPEMKeyConverter.getPrivateKey(privateKeyInfo);
            } else {
                throw new IllegalArgumentException("Unsupported private key format '" +
                        pemContent.getClass().getSimpleName() + '"');
            }
        }

        return key;
    }

    public static String getJwt() {
        String appId = "319865";

        //String appId = "leocode-repos";

        // issued at time, 60 seconds in the past to allow for clock drift
        LocalDateTime iat = LocalDateTime.now().minus(60, ChronoUnit.SECONDS);

        // JWT expiration time (10 minute maximum)
        LocalDateTime exp = LocalDateTime.now().plus(10 * 60, ChronoUnit.SECONDS);

        // GitHub App's identifier
        JwtPayload payload = new JwtPayload(iat, exp, appId);

        Jsonb jsonb = JsonbBuilder.create();
        String jsonPayload = jsonb.toJson(payload);

        try (FileReader fr = new FileReader("../leocode-github-integration.2023-04-17.private-key.pem")) {
            // get private key from pem file
            PrivateKey key = getPrivateKeyFromPEM(fr);

            // generate JWS
            return Jwts.builder().setSubject(jsonPayload).signWith(key).compact();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
