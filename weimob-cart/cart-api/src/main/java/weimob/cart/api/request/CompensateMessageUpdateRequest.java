package weimob.cart.api.request;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: 老张
 * @Date: 2020/4/21
 */
@Data
public class CompensateMessageUpdateRequest implements Serializable {
    private static final long serialVersionUID = -3521903118858478250L;
    /**
     * id
     */
    private Long id;
    /**
     * 明细内容
     */
    private String context;
    /**
     * 消息id
     */
    private String msgId;
    /**
     * 业务类型
     */
    private String bizType;
    /**
     * 处理状态
     */
    private Integer status;

    /**
     * 创建时间
     */
    private Date createDate;
    /**
     * 更新时间
     */
    private Date updateDate;

}
