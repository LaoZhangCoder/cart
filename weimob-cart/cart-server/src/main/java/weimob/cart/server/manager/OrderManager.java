package weimob.cart.server.manager;

import cart.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import weimob.cart.api.request.OrderRequest;
import weimob.cart.server.dao.CompensateMessageDao;
import weimob.cart.server.domain.model.CompensateMessageDo;

import java.util.Date;
import java.util.Random;

/**
 * @Author: 老张
 * @Date: 2020/4/21
 */
@Component
@Slf4j
public class OrderManager {
    @Autowired
    private CompensateMessageDao compensateMessageDao;

    private static Random random = new Random();

    private static final int TWO = 2;

    private static final int TEN = 10;


    @Transactional(rollbackFor = Exception.class)
    public Boolean createOrder(OrderRequest orderRequest) {
        try {
            Boolean orderResult = getOrderResult();
            CompensateMessageDo compensateMessageDo = new CompensateMessageDo();
            compensateMessageDo.setBizType("cart");
            compensateMessageDo.setContext("用来清除下单成功后购物车的数据！");
            compensateMessageDo.setCreateDate(new Date());
            compensateMessageDo.setUpdateDate(new Date());
            compensateMessageDo.setMsgId(orderRequest.getUserId());
            compensateMessageDo.setStatus(0);
            compensateMessageDao.create(compensateMessageDo);
            if (orderResult) {
                return true;
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            throw new ServiceException(e);
        }
        return false;
    }

    /**
     * 模拟创建订单成功或者失败
     *
     * @return
     */
    private Boolean getOrderResult() throws Exception {
        int var = random.nextInt(TEN);
        if (var % TWO == 0) {
            log.info("生成的随机数为:{}" ,var);
            throw new Exception("创建订单失败！进行事务回滚");
        } else{
            return true;
        }
    }
}


