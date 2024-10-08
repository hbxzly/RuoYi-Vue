<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="账户ID" prop="adAccountId">
        <el-input
          v-model="queryParams.adAccountId"
          placeholder="请输入账户ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="账户名称" prop="adAccountName">
        <el-input
          v-model="queryParams.adAccountName"
          placeholder="请输入账户名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主页名称" prop="pageName">
        <el-input
          v-model="queryParams.pageName"
          placeholder="请输入主页名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="主页ID" prop="pageId">
        <el-input
          v-model="queryParams.pageId"
          placeholder="请输入主页ID"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
        <el-form-item label="投放状态" prop="status">
          <el-select v-model="queryParams.status" placeholder="投放状态" clearable>
            <el-option
              v-for="dict in dict.type.advertise_status"
              :key="dict.value"
              :label="dict.label"
              :value="dict.value"
            />
          </el-select>
        </el-form-item>
      <el-form-item label="运营" prop="operation">
        <el-input
          v-model="queryParams.operation"
          placeholder="请输入运营"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="客户" prop="client">
        <el-input
          v-model="queryParams.client"
          placeholder="请输入客户"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="投放地区" prop="placementArea">
        <el-input
          v-model="queryParams.placementArea"
          placeholder="请输入投放地区"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="广告手" prop="operator">
        <el-input
          v-model="queryParams.operator"
          placeholder="请输入广告手"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
                        v-model="queryParams.startTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
                        v-model="queryParams.endTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="备注" prop="note">
        <el-input
          v-model="queryParams.note"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="授权账号" prop="authorizedAccount">
        <el-input
          v-model="queryParams.authorizedAccount"
          placeholder="请输入授权账号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="授权BM" prop="authorizedBm">
        <el-input
          v-model="queryParams.authorizedBm"
          placeholder="请输入授权BM"
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
          v-hasPermi="['system:advertise:add']"
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
          v-hasPermi="['system:advertise:edit']"
        >修改
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:advertise:remove']"
        >删除
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:advertise:export']"
        >导出
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleMultipleOpenScreenshotPage"
          v-hasPermi="['account:fbAccount:edit']"
        >打开广告</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-log"
          size="mini"
          @click="handleCompleteInfo"
        >完善信息</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="advertiseList" @selection-change="handleSelectionChange"
              style="height: calc(100vh - 300px); overflow-y: auto;">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="序号" align="center" prop="keyId" width="50px"/>
      <el-table-column label="账户名称" align="center" prop="adAccountName"/>
      <el-table-column label="客户" align="center" prop="client"/>
      <el-table-column label="账户ID" align="center" prop="adAccountId"/>
      <el-table-column label="主页名称" align="center" prop="pageName"/>
      <el-table-column label="主页ID" align="center" prop="pageId"/>
      <el-table-column label="运营" align="center" prop="operation"/>
      <el-table-column label="投放内容" align="center" prop="advertisingContent"/>
      <el-table-column label="投放地区" align="center" prop="placementArea"/>
      <el-table-column label="投放状态" align="center" prop="status">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.advertise_status" :value="scope.row.status"/>
        </template>
      </el-table-column>
      <el-table-column label="账户类型" align="center" prop="adType"/>
      <el-table-column label="广告手" align="center" prop="operator"/>
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <!--<el-table-column label="结束时间" align="center" prop="endTime" width="180" >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>-->
      <el-table-column label="备注" align="center" prop="note"/>
      <el-table-column label="授权账号" align="center" prop="authorizedAccount"/>
      <el-table-column label="授权BM" align="center" prop="authorizedBm"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleOpenAdvertise(scope.row)"
            v-hasPermi="['system:advertise:edit']"
          >打开广告
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleOpenScreenshotPage(scope.row)"
            v-hasPermi="['system:advertise:remove']"
          >截图页面
          </el-button>
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


    <!-- 修改广告代投对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="账户ID" prop="adAccountId">
          <el-input v-model="form.adAccountId" placeholder="请输入账户ID"/>
        </el-form-item>
        <el-form-item label="账户名称" prop="adAccountName">
          <el-input v-model="form.adAccountName" placeholder="请输入账户名称"/>
        </el-form-item>
        <el-form-item label="主页名称" prop="pageName">
          <el-input v-model="form.pageName" placeholder="请输入主页名称"/>
        </el-form-item>
        <el-form-item label="主页ID" prop="pageId">
          <el-input v-model="form.pageId" placeholder="请输入主页ID"/>
        </el-form-item>
        <el-form-item label="投放状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio
              v-for="dict in dict.type.advertise_status"
              :key="dict.value"
              :label="dict.value"
            >{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="运营" prop="operation">
          <el-input v-model="form.operation" placeholder="请输入运营"/>
        </el-form-item>
        <el-form-item label="客户" prop="client">
          <el-input v-model="form.client" placeholder="请输入客户"/>
        </el-form-item>
        <el-form-item label="投放内容">
          <el-input v-model="form.advertisingContent" placeholder="请输入投放内容"/>
        </el-form-item>
        <el-form-item label="投放地区" prop="placementArea">
          <el-input v-model="form.placementArea" placeholder="请输入投放地区"/>
        </el-form-item>
        <el-form-item label="广告手" prop="operator">
          <el-input v-model="form.operator" placeholder="请输入广告手"/>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择开始时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择结束时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="form.note" placeholder="请输入备注"/>
        </el-form-item>
        <el-form-item label="授权账号" prop="authorizedAccount">
          <el-input v-model="form.authorizedAccount" placeholder="请输入授权账号"/>
        </el-form-item>
        <el-form-item label="授权BM" prop="authorizedBm">
          <el-input v-model="form.authorizedBm" placeholder="请输入授权BM"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 添加广告代投对话框 -->
    <el-dialog :title="title" :visible.sync="openAdd" width="500px" append-to-body>
      <el-form ref="elForm" :model="formData" size="medium" label-width="100px">
        <el-form-item label="代投" prop="advertise">
          <el-input v-model="formData.advertise" type="textarea" placeholder="格式：账户名+账户id+主页名+主页id+运营+客户+投放内容+投放地区+账户类型+投手"
                    :autosize="{minRows: 4, maxRows: 4}" :style="{width: '100%'}"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFormAdd">确 定</el-button>
        <el-button @click="cancelAdd">取 消</el-button>
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
import {
  addAdvertise,
  delAdvertise,
  getAdvertise,
  listAdvertise,
  updateAdvertise,
  openAdvertise,
  openScreenshotPage,
  multipleOpenScreenshotPage,
  CompleteInfo,
  completeInfo
} from "@/api/account/advertise";

  export default {
    name: "Advertise",

    dicts: ['advertise_status'],

    data() {
      return {

        formData: {},
        // 遮罩层
        loading: true,
        // 选中数组
        ids: [],
        // 非单个禁用
        single: true,

        openAdd:false,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // 广告代投表格数据
        advertiseList: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          adAccountId: null,
          adAccountName: null,
          pageName: null,
          pageId: null,
          operation: null,
          client: null,
          advertisingContent: null,
          placementArea: null,
          status: null,
          adType: null,
          operator: null,
          startTime: null,
          endTime: null,
          note: null,
          authorizedAccount: null,
          authorizedBm: null
        },
        // 表单参数
        form: {},
        // 表单校验
        rules: {}
      };
    },
    created() {
      this.getList();
    },

    methods: {
      /** 查询广告代投列表 */
      getList() {
        this.loading = true;
        listAdvertise(this.queryParams).then(response => {
          this.advertiseList = response.rows;
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
        this.formData.advertise = undefined;
        this.openAdd = false;
      },
      // 表单重置
      reset() {
        this.form = {
          keyId: null,
          adAccountId: null,
          adAccountName: null,
          pageName: null,
          pageId: null,
          operation: null,
          client: null,
          advertisingContent: null,
          placementArea: null,
          status: null,
          adType: null,
          operator: null,
          startTime: null,
          endTime: null,
          note: null,
          authorizedAccount: null,
          authorizedBm: null
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
        this.single = selection.length !== 1
        this.multiple = !selection.length
      },
      /** 新增按钮操作 */
      handleAdd() {
        this.title = "添加代投";
        this.openAdd = true;
      },

      /** 修改按钮操作 */
      handleUpdate(row) {
        this.reset();
        const keyId = row.keyId || this.ids
        getAdvertise(keyId).then(response => {
          this.form = response.data;
          this.open = true;
          this.title = "修改广告记录";
        });
      },

      /** 多开操作 */
      handleMultipleOpenScreenshotPage(row) {
        const ids = row.id || this.ids;
        this.$modal.confirm('是否确认打开勾选的广告？').then(function() {
          return multipleOpenScreenshotPage(ids);
        });
      },

      /** 提交按钮 */
      submitForm() {
        this.$refs["form"].validate(valid => {
          if (valid) {
            if (this.form.keyId != null) {
              updateAdvertise(this.form).then(response => {
                this.$modal.msgSuccess("修改成功");
                this.open = false;
                this.getList();
              });
            } else {
              addAdvertise(this.form).then(response => {
                this.$modal.msgSuccess("新增成功");
                this.open = false;
                this.getList();
              });
            }
          }
        });
      },

      submitFormAdd: function (){
        if (this.formData.advertise != undefined){
          addAdvertise(this.formData).then(response => {
            this.$modal.msgSuccess("添加成功");
            this.openAdd = false;
            this.getList();
          });
        }
      },

      /** 删除按钮操作 */
      handleDelete(row) {
        const keyIds = row.keyId || this.ids;
        this.$modal.confirm('是否确认删除广告代投编号为"' + keyIds + '"的数据项？').then(function () {
          return delAdvertise(keyIds);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(() => {
        });
      },

      /** 导出按钮操作 */
      handleExport() {
        this.download('system/advertise/export', {
          ...this.queryParams
        }, `advertise_${new Date().getTime()}.xlsx`)
      },

      // 更多操作
      handleCommand(command, row) {
        switch (command) {
          case "handleOpenScreenshotPage":
            this.handleOpenScreenshotPage(row);
            break;
        }
      },

      /**打开截图页面*/
      handleOpenScreenshotPage(row) {
        const id = row.keyId || this.ids
        openScreenshotPage(id).then(response => {
          this.$modal.msgSuccess("成功");
          this.getList();
        });
      },

      /** 打开浏览器操作 */
      handleOpenAdvertise(row) {
        const id = row.adAccountId || this.adAccountId
        openAdvertise(id).then(response => {
          this.$modal.msgSuccess("打开成功");
          this.getList();
        });
      },
      handleCompleteInfo(){
        completeInfo().then(response => {
          this.$modal.msgSuccess("更新成功");
          this.getList();
        })
      }

    }
  };
</script>
