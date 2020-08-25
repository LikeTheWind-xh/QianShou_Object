import com.qianshou.item.ItemApplication;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest(classes = ItemApplication.class)
@RunWith(SpringRunner.class)
public class TestRandom {

    @Test
    public void testRandom(){
        System.out.println(RandomStringUtils.random(3));
    }
}
