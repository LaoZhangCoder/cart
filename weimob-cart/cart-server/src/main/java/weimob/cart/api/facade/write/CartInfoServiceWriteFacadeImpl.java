package weimob.cart.api.facade.write;

import cart.response.Response;
import com.alibaba.dubbo.config.annotation.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import weimob.cart.api.facade.CartInfoServiceWriteFacade;
import weimob.cart.api.request.*;
import weimob.cart.api.response.CompensateMessageInfo;
import weimob.cart.server.domain.dto.CompensateMessageDto;
import weimob.cart.server.query.CompensateMessageUpdateQuery;
import weimob.cart.server.service.CartService;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author: 老张
 * @Date: 2020/3/31
 */
@Service
@Component
public class CartInfoServiceWriteFacadeImpl implements CartInfoServiceWriteFacade {

    @Autowired
    private CartService cartService;

    @Override
    public Response<Boolean> mergeCartInfo(MergeCartInfoRequest request) {
        return Response.ok(cartService.mergeCartInfo(request));
    }

    @Override
    public Response<String> updateCartInfo(CartInfoUpdateRequest request) {
        return Response.ok(cartService.updateCartInfo(request));
    }

    @Override
    public Response<String> deleteCart(CartInfoDeleteRequest request) {
        return Response.ok(cartService.deleteCart(request));
    }

    @Override
    public Response<String> removeCartList(CartInfoListRemoveRequest request) {
        return Response.ok(cartService.removeCartList(request));
    }

    @Override
    public Response<String> submitCartOrder(OrderRequest request) {
        return Response.ok(cartService.submitCartOrder(request));
    }
    @Override
    public Response<List<CompensateMessageInfo>> listCompensateMessageDto() {
        return getListResponse();
    }

    @Override
    public Response<String> updateCompensateMessage(CompensateMessageUpdateRequest query) {
        CompensateMessageUpdateQuery compensateMessageUpdateQuery = new CompensateMessageUpdateQuery();
        BeanUtils.copyProperties(query,compensateMessageUpdateQuery);
        return Response.ok(cartService.updateCompensateMessage(compensateMessageUpdateQuery));
    }

    private Response<List<CompensateMessageInfo>> getListResponse() {
        List<CompensateMessageInfo> collect = null;
        List<CompensateMessageDto> compensateMessageDtoList = cartService.listCompensateMessageDto();
        if (compensateMessageDtoList != null && !compensateMessageDtoList.isEmpty()) {
            collect = compensateMessageDtoList.stream().map(compensateMessageDto -> {
                CompensateMessageInfo compensateMessageInfo = new CompensateMessageInfo();
                BeanUtils.copyProperties(compensateMessageDto, compensateMessageInfo);
                return compensateMessageInfo;
            }).collect(Collectors.toList());
        }
        return Response.ok(collect);
    }
}