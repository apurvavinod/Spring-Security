package io.javabrains.springsecurityjwt.service;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.json.JSONObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.javabrains.springsecurityjwt.exception.AppServiceException;

@Service
public class JwtUtilService {
	private static final String SECRET_STR = "secret";
	
	private static final String JWT_HEADER_STR = "{\r\n"
			+ "    \"alg\": \"HS256\",\r\n"
			+ "    \"typ\": \"JWT\"\r\n"
			+ "}";
	
	public String generateToken(UserDetails userDetails) {	
		JSONObject header = new JSONObject(JWT_HEADER_STR);	
		JSONObject payload = new JSONObject();
		payload.put("userName", userDetails.getUsername());
		payload.put("exp", System.currentTimeMillis() + ((3600 * 1000) * 10));
		
		String base64EncodedHeader = encodeStr(header.toString());
		String base64EncodedPayload = encodeStr(payload.toString());
		
		String signature = hmacSha256(base64EncodedHeader + "." + base64EncodedPayload, SECRET_STR);
		
		String jwtToken = base64EncodedHeader + "." + base64EncodedPayload + "." + signature;
		return jwtToken;
	}	
	
	private String encodeStr(String inputString) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(inputString.getBytes(StandardCharsets.UTF_8));
	}
	
	private String encode(byte[] bytes) {
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
	
	private String hmacSha256(String inputData, String secretString) {
		try {
			/* Get a Secret Key from the Secret */
			byte[] secretStringBytes = secretString.getBytes(StandardCharsets.UTF_8);
			SecretKeySpec secretKeySpec = new SecretKeySpec(secretStringBytes, "HmacSHA256");
			
			/* Get a Mac and initialize it using the Secret Key */
			/* Some Hashing functions require a key and some don't */
			Mac sha256Mac = Mac.getInstance("HmacSHA256");
			sha256Mac.init(secretKeySpec);
			
			/* Pass inputData to the Mac and get sign.  Encode this and return it */
			byte[] signBytes = sha256Mac.doFinal(inputData.getBytes(StandardCharsets.UTF_8));
			return encode(signBytes);
			
			/*Some Hashing functions require a key, and some don't.  To generate a signature, we use a Hashing function with a Key.  This function gives different behaviour for different Keys.
			Comparing this with the Digital Signature explanation (in book),
			For JWT, Signature = HMACSHA(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret) 
			      => Signature = HMACSHA(message, secret)
				consider message = base64UrlEncode(header) + "." + base64UrlEncode(payload) 
			This is doing both hashing of the message and encryption of the message using secret.
			From the secret, a Secret Key (Private Key) is obtained.
			The Hashing algorithm is initialized using this Secret Key.
			Then the encryption of the message is done by using this Hashing algorithm.
			The resulting signature bytes are base64 encoded and returned.*/
		}
		catch (NoSuchAlgorithmException | InvalidKeyException e) {
			throw new AppServiceException("Error when signing");
		}
	}
	
	public Boolean validateToken(String jwtToken, UserDetails userDetails) { 
		String[] tokenParts = jwtToken.split("\\.");		
		String base64EncodedHeader = tokenParts[0];
		String base64EncodedPayload = tokenParts[1];
		String base64EncodedSignature = tokenParts[2];
		
		String derivedSignature = hmacSha256(base64EncodedHeader + "." + base64EncodedPayload, SECRET_STR);
		if (derivedSignature.compareTo(base64EncodedSignature) == 0) {
			JSONObject payload = new JSONObject(decodeStr(tokenParts[1]));
			
			if (payload.getLong("exp") > System.currentTimeMillis()) {
				return (payload.getString("userName").equalsIgnoreCase(userDetails.getUsername()));
			}
		}		
		return false;
	}
	 
	
	private String decodeStr(String inputString) {
		return new String(Base64.getUrlDecoder().decode(inputString));
	}
	
	public String extractUserName(String jwtToken) {
		String[] tokenParts = jwtToken.split("\\.");
		String base64EncodedPayload = tokenParts[1];
		
		String payloadStr = decodeStr(base64EncodedPayload);
		JSONObject payload = new JSONObject(payloadStr);
		return payload.getString("userName");
	}
}
