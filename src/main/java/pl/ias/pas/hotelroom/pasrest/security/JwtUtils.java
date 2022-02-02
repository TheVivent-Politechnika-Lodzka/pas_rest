package pl.ias.pas.hotelroom.pasrest.security;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import javax.security.enterprise.identitystore.CredentialValidationResult;
import java.util.Date;

public class JwtUtils {

    // 256bit secret
    private static final String SECRET = "zjZi6JWZ99IT0Trx49MNitLpwPjQc81BOUZytttWprg=";
    private static final int EXPIRATION = 15 * 60 * 1000;

    public static String generateJwtString(CredentialValidationResult credential) {
        try {
            JWSSigner signer = new MACSigner(SECRET);

            JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                    .subject(credential.getCallerPrincipal().getName())
                    .claim("auth", String.join(",", credential.getCallerGroups()))
                    .issuer("PAS REST API")
                    .expirationTime(new Date(System.currentTimeMillis() + EXPIRATION))
                    .build();

            SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

            signedJWT.sign(signer);

            return signedJWT.serialize();

        } catch (JOSEException e) {
            return "JWT generation failed";
        }
    }


    public static boolean validateJwtToken(String token) {
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            return signedJWT.verify(new MACVerifier(SECRET));
        } catch (Exception e) {
            return false;
        }
    }


}
