import com.qianshou.recommend.RecommendApplication;
import com.qianshou.recommend.pojo.qianshouUser;
import com.qianshou.recommend.service.RecommendUser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import utlis.Result;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RecommendApplication.class)
public class TestRecommend {

    @Autowired
    private RecommendUser recommendUser;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Test
    public void selectMaxScore(){
        qianshouUser user = recommendUser.queryScoreMax(1L);
        System.out.println(user);

    }
}
