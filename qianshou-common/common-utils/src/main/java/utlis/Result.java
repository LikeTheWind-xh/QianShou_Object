package utlis;




import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {

    @ApiModelProperty(value = "是否成功")
    private Boolean success;

    @ApiModelProperty(value = "返回状态码",example = "1成功 0失败")
    private Integer code;

    @ApiModelProperty("返回消息")
    private String message;

    @ApiModelProperty("返回数据")
    private Map<String,Object> data = new HashMap<String, Object>();

    private Result(){};

    public static Result ok(){
        Result result = new Result();
        result.setSuccess(true);
        result.setCode(1);
        result.setMessage("成功");
        return  result;
    }

    public static Result error(){
        Result result = new Result();
        result.setSuccess(false);
        result.setCode(0);
        result.setMessage("失败");
        return  result;
    }
    public Result message(String message){
        this.message=message;
        return this;
    }
    public Result data(String key, Object value){
        this.data.put(key,value);
        return this;
    }

    public Result data(Map<String,Object> map){
        this.setData(map);
        return this;
    }
}
