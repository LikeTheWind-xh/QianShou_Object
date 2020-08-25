import org.apache.commons.codec.digest.DigestUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import com.qianshou.sso.QianShouSsoApplication;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = QianShouSsoApplication.class)
public class test {

    @Test
    public void testmd5(){
        String qianshouba = DigestUtils.md5Hex("qianshouba");
        System.out.println(qianshouba);
    }
}
