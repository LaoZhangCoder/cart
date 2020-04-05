<template xmlns:v-slot="http://www.w3.org/1999/XSL/Transform">
  <div>
    <nav-header></nav-header>
    <div class="nav-breadcrumb-wrap">
      <div class="container">
        <nav class="nav-breadcrumb">
          <a href="/">首页</a>
          <span>购物车</span>
        </nav>
      </div>
    </div>
    <!-- 定义购物车页面图标 -->
    <svg style="position: absolute; width: 0; height: 0; overflow: hidden;" version="1.1"
         xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink">
      <defs>
        <symbol id="icon-ok" viewBox="0 0 32 32">
          <title>ok</title>
          <path class="path1"
                d="M31.020 0.438c-0.512-0.363-1.135-0.507-1.757-0.406s-1.166 0.435-1.529 0.937l-17.965 24.679-5.753-5.67c-0.445-0.438-1.035-0.679-1.664-0.679s-1.219 0.241-1.664 0.679c-0.917 0.904-0.917 2.375 0 3.279l7.712 7.6c0.438 0.432 1.045 0.681 1.665 0.681l0.195-0.008c0.688-0.057 1.314-0.406 1.717-0.959l19.582-26.9c0.754-1.038 0.512-2.488-0.538-3.233z">
          </path>
        </symbol>
        <symbol id="icon-del" viewBox="0 0 32 32">
          <title>delete</title>
          <path class="path1"
                d="M11.355 4.129v-2.065h9.29v2.065h-9.29zM6.194 29.935v-23.742h19.613v23.742h-19.613zM30.968 4.129h-8.258v-3.097c0-0.569-0.463-1.032-1.032-1.032h-11.355c-0.569 0-1.032 0.463-1.032 1.032v3.097h-8.258c-0.569 0-1.032 0.463-1.032 1.032s0.463 1.032 1.032 1.032h3.097v24.774c0 0.569 0.463 1.032 1.032 1.032h21.677c0.569 0 1.032-0.463 1.032-1.032v-24.774h3.097c0.569 0 1.032-0.463 1.032-1.032s-0.463-1.032-1.032-1.032v0z">
          </path>
          <path class="path2"
                d="M10.323 9.806c-0.569 0-1.032 0.463-1.032 1.032v14.452c0 0.569 0.463 1.032 1.032 1.032s1.032-0.463 1.032-1.032v-14.452c0-0.569-0.463-1.032-1.032-1.032z">
          </path>
          <path class="path3"
                d="M16 9.806c-0.569 0-1.032 0.463-1.032 1.032v14.452c0 0.569 0.463 1.032 1.032 1.032s1.032-0.463 1.032-1.032v-14.452c0-0.569-0.463-1.032-1.032-1.032z">
          </path>
          <path class="path4"
                d="M21.677 9.806c-0.569 0-1.032 0.463-1.032 1.032v14.452c0 0.569 0.463 1.032 1.032 1.032s1.032-0.463 1.032-1.032v-14.452c0-0.569-0.463-1.032-1.032-1.032z">
          </path>
        </symbol>
      </defs>
    </svg>
    <div class="container">
      <div class="cart">
        <div class="page-title-normal">
          <h2 class="page-title-h2"><span>我的购物车</span></h2>
        </div>
        <div class="item-list-wrap">
          <div class="cart-item">
            <div class="cart-item-head">
              <ul>
                <li>商品信息</li>
                <li>商品金额</li>
                <li>购买数量</li>
                <li>总金额</li>
                <li>编辑</li>
              </ul>
            </div>
            <ul class="cart-item-list">
              <li v-for="item in cartList" :key="item.goodsId">
                <div class="cart-tab-1">
                  <div class="cart-item-check">
                    <a href="javascipt:;" class="checkbox-btn item-check-btn"
                       :class="{'checked':item.checked}" @click="editCart('checked',item)">
                      <svg class="icon icon-ok">
                        <use xlink:href="#icon-ok"></use>
                      </svg>
                    </a>
                  </div>
                  <div class="cart-item-pic">
                    <img
                      src="//img14.360buyimg.com/n7/jfs/t1/96505/26/15113/79912/5e6ec8a5Eb6d8546c/0cc2d2120993ffaf.jpg">
                  </div>
                  <div class="cart-item-title">
                    <div class="item-name">{{item.goodsName}}</div>
                  </div>
                </div>
                <div class="cart-tab-2">
                  <div class="item-price">{{item.goodsPrice}}</div>
                </div>
                <div class="cart-tab-3">
                  <div class="item-quantity">
                    <div class="select-self select-self-open">
                      <div class="select-self-area">
                        <el-input-number :step="1" v-model="item.count" @change="handleChange(item)" :min="1"
                                         :max="item.goodsNum"></el-input-number>
                      </div>
                    </div>
                  </div>
                </div>
                <div class="cart-tab-4">
                  <div class="item-price-total">{{item.goodsPrice*item.count | currency}}</div>
                </div>
                <div class="cart-tab-5">
                  <div class="cart-item-opration">
                    <a href="javascript:;" class="item-edit-btn" @click="delCartConfirm(item)">
                      <svg class="icon icon-del">
                        <use xlink:href="#icon-del"></use>
                      </svg>
                    </a>
                  </div>
                </div>
              </li>
            </ul>
          </div>
        </div>
        <div class="cart-foot-wrap">
          <div class="cart-foot-inner">
            <div class="cart-foot-l">
              <div class="item-all-check">
                <a href="javascipt:;" @click="toggleCheckAll">
                <span class="checkbox-btn item-check-btn" v-bind:class="{'check':checkAllFlag}">
                  <svg class="icon icon-ok">
                    <use xlink:href="#icon-ok"/></svg>
                </span>
                  <span>全选</span>
                </a>
              </div>
            </div>
            <div class="cart-foot-r">
              <div class="item-total">
                总价: <span class="total-price">{{totalPrice | currency}}</span>
              </div>
              <div class="btn-wrap">
                <a class="btn btn--red" :class="{'btn--dis':!checkedCount}" @click="checkOut">结算</a>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <nav-footer></nav-footer>
    <modal :mdShow="modalConfirm" @close="closeModal">
      <template v-slot:message>
        <p>你确认要删除此条数据吗?</p>
      </template>
      <template v-slot:btnGroup>
        <a class="btn btn--m" href="javascript:;" v-on:click="delCart()">确认</a>
        <a class="btn btn--m btn--red" href="javascript:;" @click="modalConfirm=false">关闭</a>
      </template>
    </modal>
  </div>
</template>
<style scoped>
  .itxt {
    text-align: center;
    font-size: 12px;
    color: #333;
    display: inline-block;
    width: 42px;
    min-width: 30px;
  }
</style>
<script>
  import NavHeader from '@/components/HeaderCommon.vue'
  import NavFooter from '@/components/Footer.vue'
  import Modal from '@/components/Modal.vue'

  export default {
    name: 'Cart',
    data() {
      return {
        modalConfirm: false,
        delItem: '',
        cartList: [],
        updateCartParam: {
          isChecked: false,
          isAllChecked: false,
          isCount: false,
          count: null,
          skuId: null
        }
      }
    },
    components: {
      NavHeader,
      NavFooter,
      Modal
    },
    mounted: function () {
      this.init();
    },
    computed: {
      checkAllFlag() {
        return this.cartList.every((item) => {
          return item.checked;
        });
      },
      checkedCount() {
        return this.cartList.some((item) => {
          return item.checked;
        })
      },
      totalPrice() {
        let money = 0;
        this.cartList.forEach((item) => {
          if (item.checked) {
            money += item.goodsPrice * item.count;
          }
        });
        return money;
      }
    },
    filters: {
      currency(value) {
        if (!value) return 0.00;
        return '￥' + value.toFixed(2) + '元';
      }
    },
    methods: {
      init() {
        this.$ajax.get("/api/cart/list").then((response) => {
          let res = response.data;
          this.cartList = res.result;
        })
      },
      editCart(type, item) {
        if (type == 'add') {
          item.count++;
        } else if (type == 'minus') {
          item.count--;
        } else {
          item.checked = !item.checked;
          this.updateCartParam.isChecked=true
          this.updateCartParam.isAllChecked=false
          this.updateCartParam.isCount=false
          this.updateCartParam.skuId=item.skuId
          this.$ajax.patch("/api/cart/updateCartInfo",this.updateCartParam).then(() => {
          })
        }
      },
      allSelected(){
        this.updateCartParam.isChecked=true
        this.updateCartParam.isAllChecked=true
        this.updateCartParam.isCount=false
        this.$ajax.patch("/api/cart/updateCartInfo",this.updateCartParam).then(() => {
        })
      },
      handleChange(item) {
        if (item.count <= 0) {
          alert("购买数量不能小于0！")
        } else if (item.count > 101) {
          alert("购买数量超过了库存！")
        } else {
          this.updateCartParam.isChecked=false
          this.updateCartParam.isAllChecked=false
          this.updateCartParam.isCount=true
          this.updateCartParam.count=item.count
          this.updateCartParam.skuId=item.skuId
          this.$ajax.patch("/api/cart/updateCartInfo",this.updateCartParam).then(() => {
          })
        }

      },
      delCartConfirm(item) {
        this.delItem = item;
        this.modalConfirm = true;

      },
      closeModal() {
        this.modalConfirm = false;
      },
      delCart() {
        let delItem = this.delItem;
        let cartId=this.delItem.id
        this.cartList.forEach((item, index) => {
          if (delItem.id == item.id) {
            this.cartList.splice(index, 1);
            this.modalConfirm = false;
          }
        })
        this.$ajax.delete("/api/cart/deleteCart/"+cartId).then(() => {
        })
      },
      toggleCheckAll() {
        let flag = !this.checkAllFlag;
        this.cartList.forEach((item) => {
          item.checked = flag;
        })
        this.allSelected()
      },
      checkOut() {
        if (this.checkedCount) {
          this.$router.push({
            path: '/address'
          })
        }
      }
    }

  }
</script>
