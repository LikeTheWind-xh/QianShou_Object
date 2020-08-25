import com.qianshou.elasticsearch.EsApplication;
import com.qianshou.elasticsearch.dao.ElasticSearchDao;
import com.qianshou.elasticsearch.pojo.ElasticsearchUser;
import org.elasticsearch.index.query.QueryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.data.elasticsearch.core.query.GetQuery;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = EsApplication.class)
public class TestElasticsearch {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    ElasticSearchDao elasticSearchDao;

    @Test
    public void createIndex(){
//        elasticsearchTemplate.createIndex(ElasticsearchUser.class);
//        elasticsearchTemplate.putMapping(ElasticsearchUser.class);
        GetQuery getQuery = new GetQuery();
        getQuery.setId("6");
        ElasticsearchUser elasticsearchUser = elasticsearchTemplate.queryForObject(getQuery, ElasticsearchUser.class);
        GeoPoint geoPoint = new GeoPoint(30.567085,103.958911);
        elasticsearchUser.setUserLocation(geoPoint);
        elasticsearchUser.setCity("四川省成都市双流区双流国际机场");
        System.out.println(elasticsearchUser);
        elasticSearchDao.save(elasticsearchUser);
//        elasticSearchDao.save();
    }

}
