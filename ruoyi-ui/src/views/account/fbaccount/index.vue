<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="序号" prop="keyId">
        <el-input
          v-model="queryParams.keyId"
          placeholder="请输入序号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="ID" prop="id">
        <el-input
          v-model="queryParams.id"
          placeholder="请输入账号ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入账号邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="账号状态" clearable>
          <el-option
            v-for="dict in dict.type.account_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="备注" prop="note">
        <el-input
          v-model="queryParams.note"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器" prop="browserStatus">
        <el-select v-model="queryParams.browserStatus" placeholder="浏览器状态" clearable>
          <el-option
            v-for="dict in dict.type.browser_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['account:fbAccount:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['account:fbAccount:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['account:fbAccount:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleMultipleOpen"
          v-hasPermi="['account:fbAccount:edit']"
        >打开</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="default"
          plain
          icon="el-icon-document"
          size="mini"
          @click="handleCloseAll"
        >关闭全部</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="default"
          plain
          icon="el-icon-edit"
          :disabled="multiple"
          size="mini"
          @click="handleMoreOperate"
        >更多操作</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleBatchAddFriend"
        >批量添加好友</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleCheckAccountInfo"
        >检查状态信息</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fbAccountList" @selection-change="handleSelectionChange" style="height: calc(100vh - 300px); overflow-y: auto;" >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="keyId" width="50px"/>
      <el-table-column label="名字" align="center" prop="name" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="邮箱" align="center" prop="email" />
      <el-table-column label="邮箱密码" align="center" prop="emailPassword" />
      <el-table-column label="秘钥" align="center" prop="secretKey" />
      <el-table-column label="状态" align="center" prop="status" width="80px">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.account_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="note" />
      <el-table-column label="浏览器状态" align="center" prop="browser_status" width="100px">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.browser_status" :value="scope.row.browserStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleOpen(scope.row)"
            v-hasPermi="['account:fbAccount:edit']"
          >打开</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-user"
            @click="handleShow(scope.row)"
            v-hasPermi="['account:fbAccount:remove']"
          >显示</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleCheckAccount" icon="el-icon-user">查看账号品质</el-dropdown-item>
              <el-dropdown-item command="handleOpenAds" icon="el-icon-user">查看广告</el-dropdown-item>
              <el-dropdown-item command="handleCheckBM" icon="el-icon-user">查看BM</el-dropdown-item>
              <el-dropdown-item command="handleCollectAdsInfo" icon="el-icon-user">收集广告账号信息</el-dropdown-item>
              <el-dropdown-item command="handleCreateBM" icon="el-icon-user">创BM</el-dropdown-item>
              <el-dropdown-item command="handleClose" icon="el-icon-user">关闭浏览器</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <!-- 修改对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" label-width="80px">
        <el-form-item label="ID" prop="id">
          <el-input v-model="form.id"/>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码"/>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱"/>
        </el-form-item>
        <el-form-item label="邮箱密码" prop="emailPassword">
          <el-input v-model="form.emailPassword" placeholder="请输入邮箱密码"/>
        </el-form-item>
        <el-form-item label="账号生日" prop="birthday">
          <el-input v-model="form.birthday" placeholder="账号生日"/>
        </el-form-item>
        <el-form-item label="账号状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.account_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注"/>
        </el-form-item>
        <el-form-item label="秘钥" prop="secretKey">
          <el-input v-model="form.secretKey" placeholder="请输入秘钥"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 更多操作对话框 -->
    <el-dialog :title="title" :visible.sync="openOperate" width="500px" append-to-body>
      <el-select v-model="operate" placeholder="选择操作" style="margin-right: 10px;" clearable>
        <el-option
          v-for="dict in dict.type.fbaccount_operate"
          :key="dict.value"
          :label="dict.label"
          :value="dict.value"
        />
      </el-select>
      <!-- 通过 style 添加右侧间距 -->
      <el-button type="primary" @click="submitOperate">确 定</el-button>
      <el-button @click="cancelOperate">取 消</el-button>
    </el-dialog>

    <!-- 添加账号对话框 -->
    <el-dialog :title="title" :visible.sync="openAdd" width="500px" append-to-body>
      <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
        <el-form-item label="账号" prop="account">
          <el-input v-model="formData.account" type="textarea" placeholder="格式：邮箱-密码-邮箱密码-账号生日-ID-秘钥-UA-缓存位置(F:\browser\)-文件名称-账号昵称"
                    :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormAdd">确 定</el-button>
        <el-button @click="cancelAdd">取 消</el-button>
      </div>
    </el-dialog>

    <!--批量添加好友-->
    <el-dialog :title="title" :visible.sync="openBatchAddFriend" width="700px" append-to-body>
      <el-form ref="elForm" :model="formData"  size="medium" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="添加账号" prop="friendAccount">
              <el-input type="textarea" v-model="formData.friendAccount" placeholder="添加账号"  clearable :style="{width: '100%'}" :rows="5">
              </el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitBatchAddFriend">确 定</el-button>
        <el-button @click="cancelBatchAdd">取 消</el-button>
      </div>
    </el-dialog>



  </div>
</template>

<style>
  /* 固定表头 */
  .el-table__header-wrapper {
    position: sticky;
    top: 0;
    z-index: 1;
    background-color: #f5f7fa; /* 设置表头背景色 */
  }
</style>



<script>
import { listFbAccount, getAccount, updateAccount, addAccount, delFbaccount,
      openBrowser, moreOperate, collectAdsInfo, multipleOpenBrowser, closeBrowser, closeAllBrowser,
      showBrowser, createBM, checkBM, checkAccount, openAds,  batchAddFriend, checkAccountInfo} from "@/api/account/fbAccount";


export default {
  name: "FbAccount",
  dicts:['account_status','browser_status','fbaccount_operate'],
  data() {
    return {

      formData: {},

      openOperate:false,

      openAdd:false,

      openAddFriend:false,

      openBatchAddFriend:false,
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 账号表格数据
      fbAccountList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,

      operate: undefined,

      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyId: undefined,
        id: undefined,
        email: undefined,
        status: undefined,
        browserStatus: undefined
      },
      // 表单参数
      form: {},

      fbAccount:{},

    };
  },
  created() {
    this.getList();
  },
  methods: {

    //查询岗位列表
    getList() {
      this.loading = true;
      listFbAccount(this.queryParams).then(response => {
        this.fbAccountList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },

    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // 取消按钮
    cancelAdd() {
      this.formData.account = undefined;
      this.openAdd = false;
    },

    // 取消按钮
    cancelBatchAdd() {
      this.formData.operationAccount = undefined;
      this.formData.friendAccount = undefined;
      this.openBatchAddFriend = false;
    },

    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleCheckAccount":
          this.handleCheckAccount(row);
          break;
        case "handleOpenAds":
          this.handleOpenAds(row);
          break;
        case "handleCheckBM":
          this.handleCheckBM(row);
          break;
        case "handleCollectAdsInfo":
          this.handleCollectAdsInfo(row);
          break;
        case "handleCreateBM":
          this.handleCreateBM(row);
          break;
        case "handleClose":
          this.handleClose(row);
          break;
        default:
          break;
      }
    },

    // 表单重置
    reset() {
      this.form = {
        keyId: undefined,
        id: undefined,
        password: undefined,
        email: undefined,
        emailPassword: undefined,
        status: "0",
        birthday: undefined,
        secretKey: undefined
      };
      this.resetForm("form");
    },

    //搜索按钮操作
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },

    //重置按钮操作
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },

    //多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!=1
      this.multiple = !selection.length
    },

    //修改按钮操作
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getAccount(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改账号";
      });
    },

    //新增按钮操作
    handleAdd() {
      this.title = "添加账号";
      this.openAdd = true;
    },

    //批量添加好友
    handleBatchAddFriend(){
      this.title = "批量添加好友";
      this.openBatchAddFriend = true;
    },

    //更多操作按钮
    handleMoreOperate(){
      this.title = "更多操作";
      this.openOperate = true;
    },

    //取消更多操作
    cancelOperate(){
      this.openOperate = false;
    },

    //打开浏览器操作
    handleOpen(row) {
      const id = row.id || this.ids
      openBrowser(id).then(response => {
        this.$modal.msgSuccess("打开成功");
        this.getList();
      });
    },

    //创BM
    handleCreateBM(row) {
      const id = row.id || this.ids
      createBM(id).then(response => {
      });
    },

    //进BM
    handleCheckBM(row){
      const id = row.id || this.ids
      checkBM(id).then(response => {
      });
    },

    //收集广告信息
    handleCollectAdsInfo(row){
      const id = row.id || this.ids
      collectAdsInfo(id).then(response => {
      });
    },

    //查看账户品质
    handleCheckAccount(row){
      const id = row.id || this.ids
      checkAccount(id).then(response => {
      });
    },

    //打开广告
    handleOpenAds(row){
      const id = row.id || this.ids
      openAds(id).then(response => {
      });
    },

    //显示浏览器操作
    handleShow(row) {
      const id = row.id || this.ids
      showBrowser(id).then(response => {
        this.$modal.msgSuccess("显示成功");
      });
    },

    //关闭浏览器操作
    handleClose(row) {
      const id = row.id || this.ids
      closeBrowser(id).then(response => {
        this.$modal.msgSuccess("关闭成功");
        this.getList();
      });
    },

    //修改提交按钮
    submitForm: function() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != undefined) {
            updateAccount(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },

    //操作提交
    submitOperate: function (row) {
      const ids = row.id || this.ids
      const operateId = this.operate
      moreOperate(ids,operateId).then(
        this.openOperate = false,
        response => {

      });
    },

    //添加账号提交
    submitFormAdd: function (){
      if (this.formData.account != undefined){
        addAccount(this.formData).then(response => {
          this.$modal.msgSuccess("添加成功");
          this.openAdd = false;
          this.getList();
        });
      }
    },

    //提交批量添加好友
    submitBatchAddFriend: function () {

      const operationAccount = this.ids;
      const friendAccount = this.formData.friendAccount;
      if ( operationAccount != undefined && friendAccount != ""){
        batchAddFriend(operationAccount, friendAccount).then(
          this.cancelBatchAdd(),
          response =>{

          });
      }
    },

    //删除按钮操作
    handleDelete(row) {
      const fbaccountIds = row.id || this.ids;
      this.$modal.confirm('是否确认删除ID为"' + fbaccountIds + '"的数据项？').then(function() {
        return delFbaccount(fbaccountIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },

    //多开操作
    handleMultipleOpen(row) {
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认打开账号ID为"' + ids + '"的账号？').then(function() {
        return multipleOpenBrowser(ids);
      });
    },

    //关闭全部
    handleCloseAll() {
      this.$modal.confirm('是否确认关闭所有？').then(function() {
        closeAllBrowser();
        this.$modal.msgSuccess("关闭成功");
        this.getList();
      });
    },

    //查看账号信息状态
    handleCheckAccountInfo(){
      const ids = this.ids;
      checkAccountInfo(ids).then(response => {

      });

    }

  }
};
</script>
