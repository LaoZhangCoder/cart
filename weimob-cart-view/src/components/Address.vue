<template>
  <div class="order-form">
    <header>
      <h3>购物清单</h3>
    </header>
    <main>
      <div class="order-form-item">
        <div v-for="item in cartGoods" class="goods">
          <div class="left">
            <img
              src="https://img10.360buyimg.com/mobilecms/s500x500_jfs/t1/112788/3/723/139555/5e8fe878Ee926b117/c8e83a48f0bc1c70.jpg">
          </div>
          <div class=name>
            <p>{{ item.goodsName }}</p>
          </div>
          <div class="right">
            <p>x{{ item.count }}</p>
          </div>
          <div class="bottom">
            <p>￥{{ item.goodsPrice }}</p>
          </div>
        </div>
      </div>
      <div class="order-form-item">
        <p>合计：
          <span class="price">￥{{ pay }}</span>
        </p>
      </div>
      <div class="order-form-item">

      </div>
    </main>
    <footer class="order-form-footer">
      <div>
        <span class="price-wrap">付款<span class="price">￥{{ pay }}</span></span>
        <el-button type="danger" class="text-right" @click="toPay">提交订单<i
          class="el-icon-arrow-right el-icon--right"></i>
        </el-button>
      </div>
    </footer>
  </div>
</template>

<script>
  import NavHeader from './../components/HeaderCommon'
  import NavFooter from './../components/Footer.vue'
  import Modal from './../components/Modal.vue'

  export default {
    name: "Address",
    components: {
      NavHeader,
      NavFooter,
      Modal
    },
    data() {
      return {
        addressPlaceHolder: '请选择地址',
        selectAddress: 0,
        cartGoods: [],
        pay: 0
      }
    },
    created() {
      this.$ajax.get("/api/cart/list").then((response) => {
        let res = response.data;
        res.result.forEach((item) => {
          if (item.checked == 1) {
            this.cartGoods.push(item)
            this.pay += item.goodsPrice * item.count;
          }
        })
      })
    },
    methods: {
      toPay() {
        this.$ajax.post("/api/cart/submit").then((response) => {
          if(response.data.success){
            alert('提交成功');
            this.$router.push({
              path: '/cart'
            })
          }else{
            alert('提交订单失败！')
          }
        })
      }
    }
  }
</script>

<style lang="css" scoped>

  .order-form {
    width: 100%;
    height: 100%;
    position: absolute;
    z-index: 1001;
    background-color: #f5f5f5;
  }

  header {
    position: relative;
    height: 50px;
    line-height: 50px;
    background-color: #fff
  }

  .toProfile i {
    position: absolute;
    left: 10px;
    font-size: 20px;
    top: 50%;
    transform: translateY(-50%);
  }

  main {
    margin-top: 10px;
    text-align: left;
  }

  .order-form-item {
    padding: 10px;
    background-color: #fff;
    margin-top: 10px;
  }


  .goods {
    height: 100px;
    line-height: 100px;
    border-bottom: 1px solid #eee;
    clear: both;
  }

  .left {
    float: left;
    height: 100%;
  }

  .name {
    float: left;
    height: 100%;
  }

  img {
    height: 100%;
  }

  .right {
    float: right;
  }

  .bottom {
    color: red;
    float: right;
    margin-top: 40px;
  }

  .price {
    color: red;
    font-size: 18px;
    font-weight: 600;
    float: right;
  }

  .order-form-footer {
    position: fixed;
    width: 100%;
    height: 40px;
    line-height: 40px;
    bottom: 0;
    border-top: 1px solid #eee;
    left: 0;
    text-align: right;
    background-color: #fff;
  }

  .price-wrap {
    float: left;
    padding-left: 10px;
    font-size: 18px;
  }

  .price {
    color: red;
    font-weight: 600;
  }

  .el-button--danger {
    background-color: red;
    border: none;
    border-radius: 0;
  }

</style>
