package auth;

import java.util.Base64;

public class Base64Encoding {
    public static void main(String[] args) {
        String usernameColonPassword = "myUsername:myPassword";

        /*
        Base64 encoding is a way taking data and transforming it to a long string of plain text to be sent over the web.
         */

        // encode string to base64
        String base64Encoded = Base64.getEncoder().encodeToString(usernameColonPassword.getBytes());
        System.out.println("Encoded = " + base64Encoded);

        // decode base64 to string
        byte[] decodeBytes = Base64.getDecoder().decode(base64Encoded);
        System.out.println("Decoded = " + new String(decodeBytes));
    }
}
