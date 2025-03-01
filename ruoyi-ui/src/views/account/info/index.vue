<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="邮箱" prop="email">
        <el-input
          v-model="queryParams.email"
          placeholder="请输入邮箱"
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
      <el-form-item label="邮箱密码" prop="emailPassword">
        <el-input
          v-model="queryParams.emailPassword"
          placeholder="请输入邮箱密码"
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
      <el-form-item label="账号名称" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入账号名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="是否商城" prop="isMarketplace">
        <el-input
          v-model="queryParams.isMarketplace"
          placeholder="请输入是否商城号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="性别" prop="gender">
        <el-input
          v-model="queryParams.gender"
          placeholder="请输入性别"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="生产日期" prop="createDate">
        <el-input
          v-model="queryParams.createDate"
          placeholder="请输入生产日期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地区" prop="createIp">
        <el-input
          v-model="queryParams.createIp"
          placeholder="请输入地区"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建状态" prop="createStatus">
        <el-input
          v-model="queryParams.createStatus"
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
          v-hasPermi="['account:info:add']"
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
          v-hasPermi="['account:info:edit']"
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
          v-hasPermi="['account:info:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="info"
          plain
          icon="el-icon-upload2"
          size="mini"
          @click="handleImport"
        >导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['account:info:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleCreate"
          v-hasPermi="['facebook:createInfo:remove']"
        >创建</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleOpenBrowser"
          v-hasPermi="['facebook:createInfo:remove']"
        >打开</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleUpdateAccountInfo"
        >完善信息</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleCheckAccountActive"
        >检测</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="infoList" @selection-change="handleSelectionChange" height="500">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" prop="keyId" />
      <el-table-column label="ID" align="center" prop="id" />
      <el-table-column label="邮箱" align="center" prop="email" show-overflow-tooltip/>
      <el-table-column label="密码" align="center" prop="password" />
      <el-table-column label="邮箱密码" align="center" prop="emailPassword" show-overflow-tooltip/>
      <el-table-column label="手机" align="center" width="110" prop="phone" />
      <el-table-column label="秘钥" align="center" prop="secretKey" show-overflow-tooltip/>
      <el-table-column label="账号名称" align="center" prop="nickName" />
      <el-table-column label="生日" align="center" prop="birthday" />
      <el-table-column label="是否商城号" align="center" prop="isMarketplace" />
      <el-table-column label="性别" align="center" prop="gender" />
      <el-table-column label="生产日期" align="center" prop="createDate" />
      <el-table-column label="创建IP" align="center" prop="createIp" />
      <el-table-column label="创建状态" align="center" prop="createStatus" />
      <el-table-column label="账号其他信息" align="center" prop="accountOtherInfo" />
      <el-table-column label="备注" align="center" prop="note" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-switch-button"
            @click="handleOpenBrowser(scope.row)"
            v-hasPermi="['account:info:edit']"
          >打开</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-refresh"
            @click="handleUpdateAccountInfo(scope.row)"
            v-hasPermi="['account:info:edit']"
          >更新</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['account:info:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['account:info:remove']"
          >删除</el-button>
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

    <!-- 添加或修改创建信息对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="form.password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="邮箱密码" prop="emailPassword">
          <el-input v-model="form.emailPassword" placeholder="请输入邮箱密码" />
        </el-form-item>
        <el-form-item label="手机" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入手机" />
        </el-form-item>
        <el-form-item label="秘钥" prop="secretKey">
          <el-input v-model="form.secretKey" placeholder="请输入秘钥" />
        </el-form-item>
        <el-form-item label="ID" prop="id">
          <el-input v-model="form.id" placeholder="请输入ID" />
        </el-form-item>
        <el-form-item label="账号名称" prop="nickName">
          <el-input v-model="form.nickName" placeholder="请输入账号名称" />
        </el-form-item>
        <el-form-item label="生日" prop="birthday">
          <el-input v-model="form.birthday" placeholder="请输入生日" />
        </el-form-item>
        <el-form-item label="是否商城号" prop="isMarketplace">
          <el-input v-model="form.isMarketplace" placeholder="请输入是否商城号" />
        </el-form-item>
        <el-form-item label="性别" prop="gender">
          <el-input v-model="form.gender" placeholder="请输入性别" />
        </el-form-item>
        <el-form-item label="生产日期" prop="createDate">
          <el-input v-model="form.createDate" placeholder="请输入生产日期" />
        </el-form-item>
        <el-form-item label="地区" prop="createIp">
          <el-input v-model="form.createIp" placeholder="请输入地区" />
        </el-form-item>
        <el-form-item label="创建状态" prop="createStatus">
          <el-input v-model="form.createStatus" placeholder="请输入创建状态" />
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!--更新选项-->
    <el-dialog :title="update.title" :visible.sync="update.open" width="500px" append-to-body>
      <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleSelectAllOption">全选</el-checkbox>
      <el-checkbox-group v-model="checkedUpdateOption">
        <el-checkbox v-for="option in options" :label="option" :key="option">{{option}}</el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitUpdateOption">确 定</el-button>
        <el-button @click="update.open = false">取 消</el-button>
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
import { listInfo, getInfo, delInfo, addInfo, updateInfo, openBrowser, updateAccountInfo, createInfo, checkAccountActive} from "@/api/account/info";
import { getToken } from "@/utils/auth";

export default {
  name: "Info",
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
      // 创建信息表格数据
      infoList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        email: null,
        password: null,
        emailPassword: null,
        phone: null,
        secretKey: null,
        nickName: null,
        birthday: null,
        isMarketplace: null,
        gender: null,
        createDate: null,
        createIp: null,
        createStatus: null,
        ua: null,
        filePath: null,
        browserProfile: null,
        companyName: null,
        address: null,
        hometownAddress: null,
        accountOtherInfo: null,
        note: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
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
        url: process.env.VUE_APP_BASE_API + "/account/info/importData"
      },
      //更新选项
      update: {
        open: false,
        title: "",
      },
      checkedUpdateOption:[],
      options: ['上传头像','添加邮箱','开启双重验证','发帖'],
      isIndeterminate: true,
      checkAll: false
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询创建信息列表 */
    getList() {
      this.loading = true;
      listInfo(this.queryParams).then(response => {
        this.infoList = response.rows;
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
        email: null,
        password: null,
        emailPassword: null,
        phone: null,
        secretKey: null,
        id: null,
        nickName: null,
        birthday: null,
        isMarketplace: null,
        gender: null,
        createDate: null,
        createIp: null,
        createStatus: null,
        ua: null,
        filePath: null,
        browserProfile: null,
        companyName: null,
        address: null,
        hometownAddress: null,
        accountOtherInfo: null,
        note: null
      };
      this.resetForm("form");
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
      this.title = "添加创建信息";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const keyId = row.keyId || this.ids
      getInfo(keyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改创建信息";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.keyId != null) {
            updateInfo(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInfo(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除创建信息编号为"' + keyIds + '"的数据项？').then(function() {
        return delInfo(keyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('account/info/export', {
        ...this.queryParams
      }, `info_${new Date().getTime()}.xlsx`)
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('account/info/importTemplate', {
      }, `info_template_${new Date().getTime()}.xlsx`)
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
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    //创建
    handleCreate(row){
      const keyIds = row.keyId || this.ids;
      this.$modal.confirm('是否确认创建信息编号为"' + keyIds + '"的数据项？').then(function() {
        return createInfo(keyIds);
      }).then(() => {
        this.getList();
      }).catch(() => {});
    },
    //打开
    handleOpenBrowser(row){
      const keyIds = row.keyId || this.ids
      openBrowser(keyIds);
    },
    /*
    //完善信息
    handleUpdateAccountInfo(row){
      const keyIds = row.keyId || this.ids
      updateAccountInfo(keyIds);
    }*/

    //全选
    handleSelectAllOption(val) {
      this.checkedUpdateOption = val ? this.options : [];
      this.isIndeterminate = false;
    },
    //完善信息
    handleUpdateAccountInfo(row){
      this.update.open = true;
      this.update.title = "选择完善信息"
      this.update.currentRow = row;
    },

    //提交操作选项
    submitUpdateOption(){
      const keyIds = this.update.currentRow.keyId
        ? (Array.isArray(this.update.currentRow.keyId) ? this.update.currentRow.keyId : [this.update.currentRow.keyId])
        : (Array.isArray(this.ids) ? this.ids : [this.ids]);
      const selectedOptions = this.checkedUpdateOption;
      this.update.open = false; // 关闭弹窗
      // 调用后端接口
      updateAccountInfo({ keyIds, selectedOptions })
        .then((response) => {
          this.$message.success("操作成功");
        })
        .catch((error) => {
          console.error("操作失败:", error);
          this.$message.error("操作失败，请重试");
        });
    },

    handleCheckAccountActive(row){
      const keyIds = row.keyId || this.ids
      checkAccountActive(keyIds).then(response => {
        this.$modal.msgSuccess("检测完毕");
        this.getList();
      });
    }
  }
};
</script>
