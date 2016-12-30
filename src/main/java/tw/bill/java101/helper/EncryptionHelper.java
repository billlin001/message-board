package tw.bill.java101.helper;

/**
 * Created by jt on 12/14/15.
 */
public interface EncryptionHelper {
    String encryptString(String input);
    boolean checkPassword(String plainPassword, String encryptedPassword);
}
