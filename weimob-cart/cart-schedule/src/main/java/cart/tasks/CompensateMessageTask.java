package cart.tasks;

import cart.response.Response;
import cart.utils.RedisClientUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import com.alibaba.dubbo.config.annotation.Reference;
import weimob.cart.api.facade.CartInfoServiceReadFacade;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.request.CartInfoDeleteRequest;
import weimob.cart.api.request.CompensateMessageUpdateRequest;
import weimob.cart.api.response.CompensateMessageInfo;

import java.util.List;

/**
 * @Author: 老张
 * @Date: 2020/4/21
 */
@Component
@Slf4j
public class CompensateMessageTask {
    @Reference
    private CartInfoServiceWriteFacade cartInfoServiceWriteFacade;
    @Autowired
    private RedisClientUtils redisClientUtils;


    @Scheduled(cron = "0/1 * * * * ?")
    public void start() {
        while (true) {
            List<CompensateMessageInfo> result = cartInfoServiceWriteFacade.listCompensateMessageDto().getResult();
            if (result == null || result.isEmpty()) {
                continue;
            }
            for (CompensateMessageInfo compensateMessageInfo : result) {
                CartInfoDeleteRequest cartInfoDeleteRequest = new CartInfoDeleteRequest();
                cartInfoDeleteRequest.setUserId(compensateMessageInfo.getMsgId());
                cartInfoDeleteRequest.setChecked(1);
                Response<String> response = cartInfoServiceWriteFacade.deleteCart(cartInfoDeleteRequest);
                if (response.isSuccess()) {
                    redisClientUtils.deleteCache("cart", compensateMessageInfo.getMsgId());
                    CompensateMessageUpdateRequest compensateMessageUpdateRequest = new CompensateMessageUpdateRequest();
                    compensateMessageUpdateRequest.setStatus(1);
                    compensateMessageUpdateRequest.setId(compensateMessageInfo.getId());
                    cartInfoServiceWriteFacade.updateCompensateMessage(compensateMessageUpdateRequest);
                }

            }

        }
    }
}
