import org.apache.commons.lang3.RandomUtils;

import java.util.Random;

public class addInfo {
    public static void main(String[] args) {
        for (int i = 0; i < 5; i++) {
            int anInt = RandomUtils.nextInt(50, 100);
            int userId = 2;
            System.out.println("db.qianshou_recommend.insert({\"userId\":"+userId+i+",\"toId\":"+1+
                    ",\"score\":"+ anInt +",\"date\":"+"\"2020/07/08\"})"
                    );
        }
    }
}
