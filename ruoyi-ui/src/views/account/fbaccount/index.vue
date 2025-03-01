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
          placeholder="请输入ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input
          v-model="queryParams.password"
          placeholder="请输入密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="邮箱密码" prop="emailPassword">
        <el-input
          v-model="queryParams.emailPassword"
          placeholder="请输入邮箱密码"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名字" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名字"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="秘钥" prop="secretKey">
        <el-input
          v-model="queryParams.secretKey"
          placeholder="请输入秘钥"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.account_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="地区" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入地区"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
      <el-form-item label="好友数量" prop="friendNumber">
        <el-input
          v-model="queryParams.friendNumber"
          placeholder="请输入好友数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="能否广告" prop="canAds">
        <el-select v-model="queryParams.canAds" placeholder="请选择能否广告" clearable>
          <el-option
            v-for="dict in dict.type.can_ads"
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
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['account:fbAccount:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
          v-hasPermi="['account:fbAccount:export']"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-star-off"
          :disabled="multiple"
          @click="handleOpenBrowser"
          size="mini"
        >打开</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-user"
          :disabled="multiple"
          @click="handleAddFriend"
          size="mini"
        >添加好友</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          :disabled="multiple"
          @click="handleCheckAccount"
          size="mini"
        >检测</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          :disabled="multiple"
          @click="handlePost"
          size="mini"
        >发帖</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-switch-button"
          size="mini"
          @click="handleCloseAllBrowser"
        >全部关闭</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-search"
          size="mini"
          @click="handleJumpPage"
        >跳页</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="fbAccountList" @selection-change="handleSelectionChange" height="500">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="keyId" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="邮箱" align="center" prop="email"
      />
      <el-table-column label="邮箱密码" align="center" prop="emailPassword" />
      <el-table-column label="名字" align="center" prop="name" />
      <el-table-column label="秘钥" align="center" prop="secretKey" show-overflow-tooltip/>
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.account_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="好友数量" align="center" prop="friendNumber" />
      <el-table-column label="能否广告" align="center" prop="canAds">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.can_ads" :value="scope.row.canAds"/>
        </template>
      </el-table-column>
      <el-table-column label="地区" align="center" prop="region" />
      <el-table-column label="备注" align="center" prop="note" />
      <el-table-column label="浏览器" align="center" prop="browserStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.browser_status" :value="scope.row.browserStatus"/>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-star-off"
            @click="handleOpenBrowser(scope.row)"
          >打开</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-star-on"
            @click="handleCloseBrowser(scope.row)"
          >关闭</el-button>
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleUpdate" v-hasPermi="['account:fbAccount:edit']" icon="el-icon-edit">修改</el-dropdown-item>
              <el-dropdown-item command="handleDelete" v-hasPermi="['account:fbAccount:remove']" icon="el-icon-delete">删除</el-dropdown-item>
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

    <!-- 添加或修改账号对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="ID" prop="id">
          <el-input v-model="form.id" placeholder="请输入ID" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="邮箱密码" prop="emailPassword">
          <el-input v-model="form.emailPassword" placeholder="请输入邮箱密码" />
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-input v-model="form.birthday" placeholder="请输入生日" />
        </el-form-item>
        <el-form-item label="名字" prop="name">
          <el-input v-model="form.name" placeholder="请输入名字" />
        </el-form-item>
        <el-form-item label="秘钥" prop="secretKey">
          <el-input v-model="form.secretKey" placeholder="请输入秘钥" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.account_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="创建日期" prop="createDate">
          <el-input v-model="form.createDate" placeholder="请输入创建日期" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" />
        </el-form-item>
        <el-form-item label="浏览器" prop="browserStatus">
          <el-radio-group v-model="form.browserStatus">
            <el-radio
              v-for="dict in dict.type.browser_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="好友数量" prop="friendNumber">
          <el-input v-model="form.friendNumber" placeholder="请输入好友数量" />
        </el-form-item>
        <el-form-item label="能否广告" prop="canAds">
          <el-radio-group v-model="form.canAds">
            <el-radio
              v-for="dict in dict.type.can_ads"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

<!--添加好友-->
    <el-dialog :title="title" :visible.sync="openAddFriend" width="500px" append-to-body>
      <el-form ref="addFriendForm" :model="addFriendFormData" :rules="addFriendRules" size="medium" label-width="100px">
        <el-form-item label="添加账号" prop="accountId">
          <el-input v-model="addFriendFormData.accountId" placeholder="请输入好友账号ID" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeAddFriend">取消</el-button>
        <el-button type="primary" @click="submitAddFriend">确定</el-button>
      </div>
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" /> 是否更新已经存在的数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size:12px;vertical-align: baseline;" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
import { listFbAccount, getFbAccount, delFbAccount, addFbAccount, updateFbAccount, openBrowser,
  closeBrowser, addFriend, checkAccount, accountPost, closeAllBrowser, jumpPage } from "@/api/account/fbAccount";
import { getToken } from "@/utils/auth";
import post from "@/views/system/post/index.vue";

export default {
  name: "FbAccount",
  dicts: ['account_status', 'can_ads', 'browser_status'],
  data() {
    return {
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
      openAddFriend: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        keyId: null,
        id: null,
        password: null,
        email: null,
        emailPassword: null,
        birthday: null,
        name: null,
        secretKey: null,
        status: null,
        createDate: null,
        note: null,
        dataSource: null,
        region: null,
        browserStatus: null,
        browserProfile: null,
        friendNumber: null,
        canAds: null
      },
      // 导入参数
      upload: {
        // 是否显示弹出层（用户导入）
        open: false,
        // 弹出层标题（用户导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的用户数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/account/fbAccount/importData"
      },
      // 表单参数
      form: {},
      addFriendFormData: {},
      // 表单校验
      rules: {
      },
      addFriendRules: {
        accountId: [{ required: true, message: '请输入好友账号ID', trigger: 'blur' }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询账号列表 */
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
    // 表单重置
    reset() {
      this.form = {
        keyId: null,
        id: null,
        password: null,
        email: null,
        emailPassword: null,
        birthday: null,
        name: null,
        secretKey: null,
        status: null,
        createDate: null,
        note: null,
        dataSource: null,
        region: null,
        browserStatus: null,
        browserProfile: null,
        friendNumber: null,
        canAds: null
      };
      this.resetForm("form");
    },
    resetAddFriendForm() {
      this.$refs.addFriendForm.resetFields();
    },

    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.keyId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加账号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const keyId = row.keyId || this.ids
      getFbAccount(keyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改账号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.keyId != null) {
            updateFbAccount(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addFbAccount(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const keyIds = row.keyId || this.ids;
      this.$modal.confirm('是否确认删除账号编号为"' + keyIds + '"的数据项？').then(function() {
        return delFbAccount(keyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('account/fbAccount/export', {
        ...this.queryParams
      }, `fbAccount_${new Date().getTime()}.xlsx`)
    },
    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleUpdate":
          this.handleUpdate(row);
          break;
        case "handleDelete":
          this.handleDelete(row);
          break;
      }
    },

    /** 打开按钮操作 */
    handleOpenBrowser(row) {
      const keyIds = row.keyId || this.ids;
      openBrowser(keyIds).then(response => {
        this.getList();
        this.$modal.msgSuccess("打开成功");
      }).catch(() => {});
    },

    /** 打开按钮操作 */
    handleCloseBrowser(row) {
      const keyIds = row.keyId || this.ids;
      closeBrowser(keyIds).then(response => {
        this.getList();
        this.$modal.msgSuccess("关闭成功");
      }).catch(() => {});
    },

    /** 添加好友按钮 */
    handleAddFriend(){
      this.title = "添加好友";
      this.openAddFriend = true;
    },

    /** 添加好友弹出窗取消按钮 */
    closeAddFriend(){
      this.openAddFriend = false;
      this.resetAddFriendForm();
    },

    /** 添加好友弹出窗确定按钮 */
    submitAddFriend(){
      const operationAccount = this.ids;
      const id = this.addFriendFormData.accountId;
      this.$refs.addFriendForm.validate((valid) => {
        if (valid) {
          this.resetAddFriendForm();// 提交后重置表单
          this.openAddFriend = false;
          addFriend(operationAccount,id);
        }
      });
    },

    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "数据导入";
      this.upload.open = true;
    },

    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },

    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", "导入结果", { dangerouslyUseHTMLString: true });
      this.getList();
    },

    /** 下载模板操作 */
    importTemplate() {
      this.download('account/fbAccount/importTemplate', {
      }, `fbAccount_template_${new Date().getTime()}.xlsx`)
    },

    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },

    //检测
    handleCheckAccount(){
      const idList = this.ids
      this.$modal.confirm('是否确认检测选中的账号？').then(function() {
        return checkAccount(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("检测成功");
      }).catch(() => {});
    },

    //发帖
    handlePost(){
      const idList = this.ids
      this.$modal.confirm('是否确认给选中的账号发帖？').then(function() {
        return accountPost(idList);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("发帖成功");
      }).catch(() => {});
    },

    //关闭全部
    handleCloseAllBrowser(){
      closeAllBrowser().then(response => {
        this.$modal.msgSuccess("全关成功");
      })
    },

    //跳转页面
    handleJumpPage(){
      jumpPage(this.queryParams).then(response => {
        if (response.data === -1){
        }else {
          this.$message.info(Math.ceil((response.data+1)/this.queryParams.pageSize));
        }
      });
    },


  }
};
</script>
