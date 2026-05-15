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
<!--      <el-form-item label="账户生日" prop="birthday">
        <el-input
          v-model="queryParams.birthday"
          placeholder="请输入账户生日"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="名字" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名字"
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
<!--      <el-form-item label="创建日期" prop="createDate">
        <el-input
          v-model="queryParams.createDate"
          placeholder="请输入创建日期"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item label="备注" prop="note">
        <el-input
          v-model="queryParams.note"
          placeholder="请输入备注"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="地区" prop="region">
        <el-input
          v-model="queryParams.region"
          placeholder="请输入地区"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="能否登录" prop="canLogin">
        <el-select v-model="queryParams.canLogin" placeholder="请选择能否登录" clearable>
          <el-option
            v-for="dict in dict.type.can_login"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否商城" prop="isMarketplace">
        <el-select v-model="queryParams.isMarketplace" placeholder="请选择是否商城号" clearable>
          <el-option
            v-for="dict in dict.type.is_marketplace"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
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
      <el-form-item label="个人户" prop="canAds">
        <el-select v-model="queryParams.adAccountStatus" placeholder="请选择能否广告" clearable>
          <el-option
            v-for="dict in dict.type.ad_account_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="是否卖出" prop="isSell">
        <el-select v-model="queryParams.isSell" placeholder="请选择是否卖出" clearable>
          <el-option
            v-for="dict in dict.type.is_sell"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="卖出日期" prop="sellDate">
        <el-date-picker
          v-model="queryParams.sellDate"
          type="date"
          placeholder="请选择卖出日期"
          format="yyyy-MM-dd"
          value-format="yyyy-MM-dd"
          clearable
        />
      </el-form-item>
      <el-form-item label="邮箱状态" prop="emailStatus">
        <el-select v-model="queryParams.emailStatus" placeholder="请选择邮箱状态" clearable>
          <el-option
            v-for="dict in dict.type.email_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item label="能否上架" prop="isShelf">
        <el-select v-model="queryParams.isShelf" placeholder="请选择是否上架" clearable>
          <el-option
            v-for="dict in dict.type.is_shelf"
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
      <el-form-item label="主页数量" prop="pageNumber">
        <el-input
          v-model="queryParams.pageNumber"
          placeholder="请输入主页数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="bm数量" prop="bmNumber">
        <el-input
          v-model="queryParams.bmNumber"
          placeholder="请输入bm数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="帖子数量" prop="postsNumber">
        <el-input
          v-model="queryParams.postsNumber"
          placeholder="请输入帖子数量"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="浏览器" prop="browserStatus">
        <el-select v-model="queryParams.browserStatus" placeholder="请选择浏览器状态" clearable>
          <el-option
            v-for="dict in dict.type.browser_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
<!--      <el-form-item label="最近帖子时间" prop="lastPostsTime">
        <el-input
          v-model="queryParams.lastPostsTime"
          placeholder="请输入最近帖子时间"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>-->
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
        <el-button icon="" size="mini" @click="modelOne">预设1</el-button>
        <el-button icon="" size="mini" @click="modelTwo">预设2</el-button>
        <el-button icon="" size="mini" @click="modelThree">预设3</el-button>
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
          v-hasPermi="['account:sell:add']"
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
          v-hasPermi="['account:sell:edit']"
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
          v-hasPermi="['account:sell:remove']"
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
          v-hasPermi="['account:sell:export']"
        >导出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-first-aid-kit"
          size="mini"
          :disabled="multiple"
          @click="handleGetAccountInfo"
        >账号信息</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-sell"
          size="mini"
          @click="handleSellAccount"
        >卖出</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-camera"
          size="mini"
          :disabled="multiple"
          @click="handlePost"
        >发帖</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-view"
          size="mini"
          @click="handleAddFriend"
        >添加好友</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-search"
          size="mini"
          @click="handleBatchSearch"
        >批量搜索</el-button>
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
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleCreatePage"
        >创建主页</el-button>
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
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleGetEmail"
        >取件</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleUnlockEmail"
        >解锁邮箱</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="multiple"
          @click="handleUnlockWSVerify"
        >解WS验证</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleChangeAccountName"
        >修改账号名字</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleLoginInPhone"
        >手机登录</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="single"
          @click="handleGetTwoFA"
        >2FA</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="multiple"
          @click="handleChangeAccountNote"
        >修改备注</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-document"
          size="mini"
          :disabled="single"
          @click="handleLoginEmail"
        >登录Email</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sellList" @selection-change="handleSelectionChange" height="800">
      <el-table-column type="expand">
        <template v-slot="props">
          <el-form label-position="top" class="demo-table-expand">
            <div class="expand-container">
              <p>账号密码： {{ props.row.password }}</p>
              <p>邮箱密码：{{ props.row.emailPassword }}</p>
              <p>秘钥：{{ props.row.secretKey }}</p>
            </div>
          </el-form>
        </template>
      </el-table-column>
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" align="center" width="60" prop="keyId" />
      <el-table-column label="ID" align="center" width="150" prop="id" />
      <el-table-column label="邮箱" align="center" width="100" prop="email" show-overflow-tooltip/>
<!--      <el-table-column  label="密码" align="center" width="155" prop="password" />
      <el-table-column  label="邮箱密码" align="center" width="100" prop="emailPassword" />-->
      <el-table-column label="名字" align="center" width="200" prop="name" />
<!--      <el-table-column  label="秘钥" align="center" prop="secretKey" show-overflow-tooltip/>
      <el-table-column  label="创建日期" align="center" prop="createDate" show-overflow-tooltip/>-->
      <el-table-column label="性别" align="center" prop="gender" show-overflow-tooltip/>
      <el-table-column label="备注" align="center" width="100" prop="note" show-overflow-tooltip/>
      <el-table-column label="地区" align="center" prop="region" show-overflow-tooltip>
        <template slot-scope="scope">
          <span style="cursor:pointer;color:#409EFF" @dblclick="handleDblClick(scope.row)">
            {{ scope.row.region }}
          </span>
        </template>
      </el-table-column>
      <el-table-column  label="能否登录" align="center" width="50" prop="canLogin">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.can_login" :value="scope.row.canLogin"/>
        </template>
      </el-table-column>
      <el-table-column  label="是否商城" align="center" width="50" prop="isMarketplace">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.is_marketplace" :value="scope.row.isMarketplace"/>
        </template>
      </el-table-column>
      <el-table-column  label="能否广告" align="center" width="50" prop="canAds">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.can_ads" :value="scope.row.canAds"/>
        </template>
      </el-table-column>
      <el-table-column  label="个人户" align="center" width="50" prop="adAccountStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.ad_account_status" :value="scope.row.adAccountStatus"/>
        </template>
      </el-table-column>
<!--      <el-table-column label="是否上架" align="center" prop="isShelf">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.is_shelf" :value="scope.row.isShelf"/>
        </template>
      </el-table-column>-->
      <el-table-column label="是否卖出" align="center" width="50" prop="isSell">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.is_sell" :value="scope.row.isSell"/>
        </template>
      </el-table-column>
      <el-table-column label="卖出日期" align="center" width="100" prop="sellDate" />
      <el-table-column label="邮箱状态" align="center" prop="emailStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.email_status" :value="scope.row.emailStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="好友数量" align="center" prop="friendNumber" width="60" show-overflow-tooltip/>
      <el-table-column label="主页数量" align="center" prop="pageNumber" width="60" show-overflow-tooltip/>
      <el-table-column label="bm数量" align="center" prop="bmNumber" width="60" show-overflow-tooltip/>
      <el-table-column label="帖子数量" align="center" prop="postsNumber" width="60" show-overflow-tooltip/>
      <el-table-column label="登录记录" align="center" prop="lastPostsTime" width="250" show-overflow-tooltip/>
      <el-table-column label="操作" align="center" width="200" class-name="small-padding fixed-width" fixed="right" >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-switch-button"
            @click="handleOpenBrowser(scope.row)"
            v-hasPermi="['account:sell:edit']"
          >打开</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-switch-button"
            @click="handleOpenHubBrowser(scope.row)"
            v-hasPermi="['account:sell:edit']"
          >hub</el-button>
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-switch-button"-->
<!--            @click="handleCloseBrowser(scope.row)"-->
<!--            v-hasPermi="['account:sell:edit']"-->
<!--          >关闭</el-button>-->
          <el-dropdown size="mini" @command="(command) => handleCommand(command, scope.row)">
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right">更多</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="handleConfirmAddFriend" v-hasPermi="['account:fbAccount:edit']" icon="el-icon-edit">同意好友</el-dropdown-item>
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

    <!-- 添加或修改卖号对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body class="sell-dialog">
      <div class="sell-dialog-body">
        <el-form ref="form" :model="form" :rules="rules" label-width="80px">
          <el-form-item label="ID" prop="id">
            <el-input v-model="form.id" placeholder="请输入ID"/>
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
          <el-form-item label="账户生日" prop="birthday">
            <el-input v-model="form.birthday" placeholder="请输入账户生日"/>
          </el-form-item>
          <el-form-item label="名字" prop="name">
            <el-input v-model="form.name" placeholder="请输入名字"/>
          </el-form-item>
          <el-form-item label="秘钥" prop="secretKey">
            <el-input v-model="form.secretKey" placeholder="请输入秘钥"/>
          </el-form-item>
          <el-form-item label="性别" prop="gender">
            <el-input v-model="form.gender" placeholder="请输入性别"/>
          </el-form-item>
          <el-form-item label="创建日期" prop="createDate">
            <el-input v-model="form.createDate" placeholder="请输入创建日期"/>
          </el-form-item>
          <el-form-item label="备注" prop="note">
            <el-input v-model="form.note" placeholder="请输入备注"/>
          </el-form-item>
          <el-form-item label="地区" prop="region">
            <el-input v-model="form.region" placeholder="请输入地区"/>
          </el-form-item>
          <el-form-item label="能否登录" prop="canLogin">
            <el-radio-group v-model="form.canLogin">
              <el-radio
                v-for="dict in dict.type.can_login"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否商城" prop="isMarketplace">
            <el-radio-group v-model="form.isMarketplace">
              <el-radio
                v-for="dict in dict.type.is_marketplace"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="能否广告" prop="canAds">
            <el-radio-group v-model="form.canAds">
              <el-radio
                v-for="dict in dict.type.can_ads"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="个人户" prop="canAds">
            <el-radio-group v-model="form.adAccountStatus">
              <el-radio
                v-for="dict in dict.type.ad_account_status"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否上架" prop="isShelf">
            <el-radio-group v-model="form.isShelf">
              <el-radio
                v-for="dict in dict.type.is_shelf"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="是否卖出" prop="isSell">
            <el-radio-group v-model="form.isSell">
              <el-radio
                v-for="dict in dict.type.is_sell"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="卖出日期" prop="sellDate">
            <el-date-picker clearable
                            v-model="form.sellDate"
                            type="date"
                            value-format="yyyy-MM-dd"
                            placeholder="请选择卖出日期">
            </el-date-picker>
          </el-form-item>
          <el-form-item label="邮箱是否可用" prop="emailStatus">
            <el-radio-group v-model="form.emailStatus">
              <el-radio
                v-for="dict in dict.type.email_status"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="好友数量" prop="friendNumber">
            <el-input v-model="form.friendNumber" placeholder="请输入好友数量"/>
          </el-form-item>
          <el-form-item label="主页数量" prop="pageNumber">
            <el-input v-model="form.pageNumber" placeholder="请输入主页数量"/>
          </el-form-item>
          <el-form-item label="bm数量" prop="bmNumber">
            <el-input v-model="form.bmNumber" placeholder="请输入bm数量"/>
          </el-form-item>
          <el-form-item label="帖子数量" prop="postsNumber">
            <el-input v-model="form.postsNumber" placeholder="请输入帖子数量"/>
          </el-form-item>
          <el-form-item label="UA" prop="ua">
            <el-input v-model="form.ua" placeholder="请输入UA"/>
          </el-form-item>
          <el-form-item label="浏览器" prop="browserStatus">
            <el-radio-group v-model="form.browserStatus">
              <el-radio
                v-for="dict in dict.type.browser_status"
                :key="dict.value"
                :label="dict.value"
              >{{ dict.label }}
              </el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
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

    <!--展示信息-->
<!--    <el-dialog :title="title" :visible.sync="sellAccount" width="700px" append-to-body>
      <el-form ref="sellForm" :model="sellForm" label-width="80px">
        &lt;!&ndash; 动态渲染字段内容 &ndash;&gt;
        <p><strong>ID：</strong>{{ sellForm.id }}</p>
        <p><strong>{{ labels.password }}：</strong>{{ sellForm.password }}</p>
        <p><strong>{{ labels.email }}：</strong>{{ sellForm.email }}</p>
        <p><strong>{{ labels.emailPassword }}：</strong>{{ sellForm.emailPassword }}</p>
        <p><strong>{{ labels.secretKey }}：</strong>{{ sellForm.secretKey }}</p>
        <p><strong>{{ labels.name }}：</strong>{{ sellForm.name }}</p>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="toggleLabels">切换语言</el-button>
        <el-button @click="copySellInfoToClipboard">复制</el-button>
        <el-button type="primary" @click="submitSellAccount">确 定</el-button>
        <el-button @click="cancelSellAccount">取 消</el-button>
      </div>
    </el-dialog>-->
    <el-dialog
      :title="title"
      :visible.sync="sellAccount"
      width="700px"
      append-to-body
      class="sell-dialog"
    >
      <div class="sell-dialog-body">
        <div v-for="(item, index) in sellFormList" :key="item.id" class="sell-item">
          <el-divider>账号 {{ index + 1 }}</el-divider>
          <el-form :model="item" label-width="80px">
            <p><strong>ID：</strong>{{ item.id }}</p>
            <p><strong>{{ labels.password }}：</strong>{{ item.password }}</p>
            <p><strong>{{ labels.email }}：</strong>{{ item.email }}</p>
            <p><strong>{{ labels.emailPassword }}：</strong>{{ item.emailPassword }}</p>
            <p><strong>{{ labels.secretKey }}：</strong>{{ item.secretKey }}</p>
            <p><strong>{{ labels.name }}：</strong>{{ item.name }}</p>
          </el-form>
        </div>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="toggleLabels">切换语言</el-button>
        <el-button @click="copySellInfoToClipboard">复制</el-button>
        <el-button type="primary" @click="submitSellAccount">确 定</el-button>
        <el-button @click="cancelSellAccount">取 消</el-button>
      </div>
    </el-dialog>

    <!--添加好友-->
    <el-dialog :title="title" :visible.sync="openAddFriend" width="500px" append-to-body>
      <el-form ref="addFriendForm" :model="addFriendFormData" :rules="addFriendRules" size="medium" label-width="100px">
        <el-form-item label="添加账号" prop="accountId">
          <el-input v-model="addFriendFormData.accountId" placeholder="请输入好友账号ID" clearable >
          </el-input>
        </el-form-item>
        <el-form-item label="开始序号" prop="startKeyId">
          <el-input v-model="addFriendFormData.startKeyId" placeholder="开始序号" clearable >
          </el-input>
        </el-form-item>
        <el-form-item label="结束序号" prop="endKeyId">
          <el-input v-model="addFriendFormData.endKeyId" placeholder="结束序号" clearable >
          </el-input>
        </el-form-item>
        <el-form-item label="地区" prop="region">
          <el-input v-model="addFriendFormData.region" placeholder="地区" clearable >
          </el-input>
        </el-form-item>
        <el-form-item label="备注" prop="note">
          <el-input v-model="addFriendFormData.note" placeholder="备注" clearable >
          </el-input>
        </el-form-item>
        <el-form-item label="添加数量" prop="number">
          <el-input v-model="addFriendFormData.number" placeholder="数量" clearable >
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeAddFriend">取消</el-button>
        <el-button type="primary" @click="submitAddFriend">确定</el-button>
      </div>
    </el-dialog>

    <!--创建主页-->
    <el-dialog :title="title" :visible.sync="openCreatePage" width="500px" append-to-body>
      <el-form ref="createPageForm" :model="createPageFormData"  size="medium" label-width="100px">
        <el-form-item label="主页名" prop="pageName">
          <el-input v-model="createPageFormData.pageName" placeholder="输入主页名" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeCreatePage">取消</el-button>
        <el-button type="primary" @click="submitCreatePage">确定</el-button>
      </div>
    </el-dialog>

    <!--创建修改名字-->
    <el-dialog :title="title" :visible.sync="openChangeAccountName" width="500px" append-to-body>
      <el-form ref="changeAccountNameForm" :model="changeAccountNameFormData"  size="medium" label-width="100px">
        <el-form-item label="账号名字" prop="accountName">
          <el-input v-model="changeAccountNameFormData.accountName" placeholder="账号名字" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeChangeAccountName">取消</el-button>
        <el-button type="primary" @click="submitChangeAccountName">确定</el-button>
      </div>
    </el-dialog>

    <!--修改备注-->
    <el-dialog :title="title" :visible.sync="openChangeAccountNote" width="500px" append-to-body>
      <el-form ref="changeAccountNoteForm" :model="changeAccountNoteFormData"  size="medium" label-width="100px">
        <el-form-item label="备注" prop="accountNote">
          <el-input v-model="changeAccountNoteFormData.accountNote" placeholder="备注" clearable :style="{width: '100%'}">
          </el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeChangeAccountNote">取消</el-button>
        <el-button type="primary" @click="submitChangeAccountNote">确定</el-button>
      </div>
    </el-dialog>

    <!--更新选项-->
    <el-dialog :title="getAccountInfo.title" :visible.sync="getAccountInfo.open" width="500px" append-to-body>
      <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleSelectAllOption">全选</el-checkbox>
      <el-checkbox-group v-model="checkedUpdateOption">
        <el-checkbox v-for="option in options" :label="option" :key="option">{{option}}</el-checkbox>
      </el-checkbox-group>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitGetOption">确 定</el-button>
        <el-button @click="getAccountInfo.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!--批量搜索-->
    <el-dialog :title="title" :visible.sync="openBatchSearch" width="500px" append-to-body>
      <el-form ref="batchSearchForm" :model="batchSearchFormData" size="medium" label-width="100px">
        <!-- 单选按钮组代替下拉框 -->
        <el-form-item label="搜索类型" prop="searchType">
          <el-radio-group v-model="batchSearchFormData.searchType">
            <el-radio label="id">账号ID</el-radio>
            <el-radio label="email">账号邮箱</el-radio>
          </el-radio-group>
        </el-form-item>
        <!-- 输入框 -->
        <el-form-item label="输入信息" prop="inputData">
          <el-input
            v-model="batchSearchFormData.inputData"
            type="textarea"
            placeholder="每行一个信息"
            rows="5"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="closeBatchSearch">取消</el-button>
        <el-button type="primary" @click="submitBatchSearch">确定</el-button>
      </div>
    </el-dialog>
  </div>


</template>


<script>
import {
  listSell,
  getSell,
  delSell,
  addSell,
  updateSell,
  checkAccount,
  updateSellForSell,
  openBrowser,
  openBitBrowser,
  openHubBrowser,
  accountPost,
  addFriend,
  jumpPage,
  checkAccountActive,
  createPage,
  confirmAddFriend,
  getEmail,
  unlockEmail,
  getAccountInfo,
  changeAccountName,
  changeAccountNote,
  batchSearch,
  get2FACode,
  getSellForShow,
  unlockWSVerify,
  loginEmail,
  loginInPhone
} from "@/api/account/sell";
import { getToken } from "@/utils/auth";

export default {
  name: "Sell",
  dicts: ['is_marketplace', 'can_login', 'can_ads', 'browser_status', 'is_shelf', 'email_status', 'is_sell', 'ad_account_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      keyIds: [],
      ids:[],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 卖号表格数据
      sellList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      //添加好友弹出层
      openAddFriend: false,
      //创建主页
      openCreatePage: false,
      //修改账号名
      openChangeAccountName: false,
      //修改备注
      openChangeAccountNote: false,
      //批量搜索弹出层
      openBatchSearch: false,
      //卖出账号
      sellAccount: false,
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
        sellDate: null,
        gender: null,
        createDate: null,
        note: null,
        region: null,
        canLogin: null,
        isMarketplace: null,
        isSell: "0",
        emailStatus: null,
        adAccountStatus: null,
        friendNumber: null,
        canAds: null,
        pageNumber: null,
        bmNumber: null,
        postsNumber: null,
        ua: null,
        browserStatus: null,
        browserProfile: null,
        filePath: null,
        lastPostsTime: null,
        isShelf: null
      },
      // 表单参数
      form: {},
      //销售表单
      sellForm: {},

      isSimplified: true,

      sellFormList: [],//多选

      labels: {
        password: "密码",
        email: "邮箱",
        emailPassword: "邮箱密码",
        secretKey: "秘钥",
        name: "名字",
      },
      //添加好友表单参数
      addFriendFormData: {
        accountId: '',
        startKeyId: '',
        endKeyId: '',
        region: '',
        note: '',
        number: ''
      },

      //创建主页参数
      createPageFormData:{
        pageName: ''
      },

      //修改账号名字
      changeAccountNameFormData:{
        accountName: ''
      },

      //修改账号名字
      changeAccountNoteFormData:{
        accountNote: ''
      },

      //批量搜索数据
      batchSearchFormData: {
        searchType: "",
        inputData: "",
        tags: [], // 用于存储生成的标签
      },
      // 表单校验
      rules: {
      },
      //添加好友校验
      addFriendRules: {
        accountId: [{ required: false, message: '请输入好友账号ID', trigger: 'blur' }],
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
        url: process.env.VUE_APP_BASE_API + "/account/sell/importData"
      },

      //更新选项
      getAccountInfo: {
        open: false,
        title: "",
      },

      checkedUpdateOption:[],
      options: ['单纯登录','名字好友','是否商城','主页数量','BM数量','广告状态','帖子数量','登录记录','台湾号'],
      isIndeterminate: true,
      checkAll: false
    };
  },
  created() {

    if (this.$route.query.id) {
      this.queryParams.id = this.$route.query.id;
      this.getList();
    }
    this.getList();
  },
  methods: {
    /** 查询卖号列表 */
    getList() {
      this.loading = true;
      listSell(this.queryParams).then(response => {
        this.sellList = response.rows;
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
        sellDate: null,
        gender: null,
        createDate: null,
        note: null,
        region: null,
        canLogin: null,
        isMarketplace: null,
        isSell: null,
        emailStatus: null,
        friendNumber: null,
        canAds: null,
        pageNumber: null,
        bmNumber: null,
        postsNumber: null,
        ua: null,
        browserStatus: null,
        browserProfile: null,
        filePath: null,
        isShelf: null
      };
      this.resetForm("form");
    },

    // 更多操作触发
    handleCommand(command, row) {
      switch (command) {
        case "handleConfirmAddFriend":
          this.handleConfirmAddFriend(row);
          break;
        case "handleUpdate":
          this.handleUpdate(row);
          break;
        case "handleDelete":
          this.handleDelete(row);
          break;
      }
    },

    //跳页清除Id
    resetId(){
      this.form = {
        keyId: null,
        id: null
      };
      this.resetForm("form");
    },

    // 添加好友表单重置
    resetAddFriendForm() {
      this.$refs.addFriendForm.resetFields();
    },

    // 创建主页表单重置
    resetCreatePageForm() {
      this.$refs.createPageForm.resetFields();
    },

 // 创建主页表单重置
    resetChangeAccountNameForm() {
      this.$refs.changeAccountNameForm.resetFields();
    },

 // 创建主页表单重置
    resetChangeAccountNoteForm() {
      this.$refs.changeAccountNoteForm.resetFields();
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
      this.keyIds = selection.map(item => item.keyId)
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加卖号";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const keyId = row.keyId || this.keyIds
      getSell(keyId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改卖号";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.keyId != null) {
            updateSell(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addSell(this.form).then(response => {
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
      const keyIds = row.keyId || this.keyIds;
      this.$modal.confirm('是否确认删除卖号编号为"' + keyIds + '"的数据项？').then(function() {
        return delSell(keyIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('account/sell/export', {
        ...this.queryParams
      }, `sell_${new Date().getTime()}.xlsx`)
    },
    /** 导入按钮操作 */
    handleImport() {
      this.upload.title = "数据导入";
      this.upload.open = true;
    },

    /** 下载模板操作 */
    importTemplate() {
      this.download('account/sell/importTemplate', {
      }, `sell_template_${new Date().getTime()}.xlsx`)
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

    //检测账号
    handleGetAccountInfo(){
      this.getAccountInfo.open = true;
      this.getAccountInfo.title = "获取账号信息"
    },

    //全选
    handleSelectAllOption(val) {
      this.checkedUpdateOption = val ? this.options : [];
      this.isIndeterminate = false;
    },

    //检测账号
    handleUpdateAccountInfoaaa(row){
      const ids = row.id || this.ids;
      this.$modal.confirm('是否确认检测选中的"'+ids.length+'"项数据？').then(function() {
        return checkAccount(ids);
      }).then(() => {
        this.$modal.msgSuccess("检测成功");
      }).catch(() => {});
    },
    //提交操作选项
    submitGetOption(){
      const keyIds = this.ids;
      const selectedOptions = this.checkedUpdateOption;
      this.getAccountInfo.open = false; // 关闭弹窗
      // 调用后端接口
      getAccountInfo({ keyIds, selectedOptions })
        .then((response) => {
          this.$message.success("操作成功");
        })
        .catch((error) => {
          console.error("操作失败:", error);
          this.$message.error("操作失败，请重试");
        });
    },

    //打开账号详细信息面板，复制卖出
    /*handleSellAccount(row){
      this.reset();
      const keyId = row.keyId || this.keyIds
      getSell(keyId).then(response => {
        this.sellForm = response.data;
        this.sellAccount = true;
        this.title = "卖出";
      });
    },*/
    handleSellAccount(row) {
      this.reset();
      const keyIds = row?.keyId ? [row.keyId] : this.ids; // 兼容单选和多选
      getSellForShow(keyIds).then(response => {
        this.sellFormList = Array.isArray(response.data) ? response.data : [response.data];
        this.sellAccount = true;
        this.title = "卖出";
      });
    },

    /*copySellInfoToClipboard() {
      const textContent = `
ID：${this.sellForm.id}
${this.labels.password}：${this.sellForm.password}
${this.labels.email}：${this.sellForm.email}
${this.labels.emailPassword}：${this.sellForm.emailPassword}
${this.labels.secretKey}：${this.sellForm.secretKey}
${this.labels.name}：${this.sellForm.name}
  `.trim();

      const textarea = document.createElement('textarea');
      textarea.value = textContent;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);

      this.$message.success("信息已复制到剪贴板");
    },*/

    copySellInfoToClipboard() {
      const textContent = this.sellFormList.map((item, index) => `
ID：${item.id}
${this.labels.password}：${item.password}
${this.labels.email}：${item.email}
${this.labels.emailPassword}：${item.emailPassword}
${this.labels.secretKey}：${item.secretKey}
${this.labels.name}：${item.name}
  `).join("").trim();

      const textarea = document.createElement('textarea');
      textarea.value = textContent;
      document.body.appendChild(textarea);
      textarea.select();
      document.execCommand('copy');
      document.body.removeChild(textarea);

      this.$message.success("所有账号信息已复制到剪贴板");
    },

    //提交卖出操作
    submitSellAccount(row) {
      const keyId = row.keyId || this.keyIds
      updateSellForSell(keyId).then(response => {
        this.$modal.msgSuccess("标记卖出成功");
        this.sellAccount = false;
        this.getList();
      });
    },

    //关闭卖出浏览器
    cancelSellAccount(){
      this.sellAccount = false;
    },

    //打开浏览器
    handleOpenBrowser(row){
      const keyId = row.keyId
      openBrowser(keyId).then(response => {

      })
    },

   //打开浏览器
    handleOpenBitBrowser(row){
      const keyId = row.keyId
      openBitBrowser(keyId).then(response => {

      })
    },

   //打开浏览器
    handleOpenHubBrowser(row){
      const keyId = row.keyId
      openHubBrowser(keyId).then(response => {

      })
    },

    //关闭浏览器
    handleCloseBrowser(row){
      const keyId = row.keyId
      closeBitBrowser(keyId).then(response => {

      })
    },

    //发帖
    handlePost(){
      const keyIds = this.keyIds
      accountPost(keyIds).then(response => {

      })
    },

    /** 添加好友按钮 */
    handleAddFriend(){
      this.title = "添加好友";
      this.openAddFriend = true;
    },

    //创建主页
    handleCreatePage(){
      this.title = "创建主页";
      this.openCreatePage = true;
    },

    //修改名字
    handleChangeAccountName(){
      this.title = "修改名字";
      this.openChangeAccountName = true;
    },

    //修改备注
    handleChangeAccountNote(){
      this.title = "修改备注";
      this.openChangeAccountNote = true;
    },

    //获取双重验证码
    handleGetTwoFA() {
      const id = this.ids; // 若依默认支持多选，取第一个
      get2FACode(id).then(respone => {
        console.log(respone)
        this.$modal.alert(`当前验证码为：${respone.msg}`, "双重验证码", {
          dangerouslyUseHTMLString: true
        });
      });
    },


    /** 添加好友弹出窗取消按钮 */
    closeAddFriend(){
      this.openAddFriend = false;
      this.resetAddFriendForm();
    },

    /** 添加好友弹出窗取消按钮 */
    closeCreatePage(){
      this.openCreatePage = false;
      this.resetCreatePageForm();
    },

    /** 添加好友弹出窗取消按钮 */
    closeChangeAccountName(){
      this.openChangeAccountName = false;
      this.resetChangeAccountNameForm();
    },
    /** 添加好友弹出窗取消按钮 */
    closeChangeAccountNote(){
      this.openChangeAccountNote = false;
      this.resetChangeAccountNoteForm();
    },


    /** 添加好友弹出窗确定按钮 */
    submitAddFriend(){
      const keyIds = this.ids;
      const id = this.addFriendFormData.accountId;
      const startKeyId = this.addFriendFormData.startKeyId;
      const endKeyId = this.addFriendFormData.endKeyId;
      const region = this.addFriendFormData.region;
      const note = this.addFriendFormData.note;
      const number = this.addFriendFormData.number;
      this.$refs.addFriendForm.validate((valid) => {
        if (valid) {
          this.resetAddFriendForm();// 提交后重置表单
          this.openAddFriend = false;
          addFriend(keyIds,id,startKeyId,endKeyId,region,note,number);
        }
      });
    },


    /** 创建主页弹出窗确定按钮 */
    submitCreatePage(){
      const operationAccount = this.ids;
      const pageName = this.createPageFormData.pageName;
      this.resetCreatePageForm();// 提交后重置表单
      this.openCreatePage = false;
      createPage(operationAccount,pageName);
    },

    /** 创建修改名字弹出窗确定按钮 */
    submitChangeAccountName(){
      const operationAccount = this.ids;
      const accountName = this.changeAccountNameFormData.accountName;
      this.resetChangeAccountNameForm();// 提交后重置表单
      this.openChangeAccountName = false;
      changeAccountName(operationAccount,accountName);
    },

    /** 创建修改备注弹出窗确定按钮 */
    submitChangeAccountNote(){
      const operationAccount = this.ids;
      const accountNote = this.changeAccountNoteFormData.accountNote;
      this.resetChangeAccountNoteForm();// 提交后重置表单
      this.openChangeAccountNote = false;
      changeAccountNote(operationAccount,accountNote);
    },


    /**批量搜索*/
    handleBatchSearch(){
      this.title = "批量搜索";
      this.openBatchSearch = true;
    },

    /**批量搜索取消*/
    closeBatchSearch(){
      this.openBatchSearch = false;
      this.batchSearchFormData.searchType = null;
      this.batchSearchFormData.inputData = null;
    },

    /**批量搜索确定*/
    submitBatchSearch(){
      const { searchType, inputData } = this.batchSearchFormData;
      if (!searchType || !inputData) {
        this.$message.error('请完整填写搜索类型和输入信息！');
        return;
      }
      // 处理输入内容，按行分割并去除前后空格
      const processedInputData = inputData
        .split('\n')
        .map(line => line.trim())
        .filter(line => line); // 去除空行
      batchSearch({
        searchType: this.batchSearchFormData.searchType,
        values: processedInputData
      }).then(response => {
        this.sellList = response.data;
        this.total = response.data.length;
        this.loading = false;
      }).catch(() => {
        this.$message.error('批量查询失败');
        this.loading = false;
      });

      this.closeBatchSearch();
    },
    //切换语言
    toggleLabels() {
      if (this.isChinese) {
        this.labels = {
          password: "密碼",
          email: "信箱",
          emailPassword: "信箱密碼",
          secretKey: "2FA",
          name: "名字",
        };
      } else {
        this.labels = {
          password: "密码",
          email: "邮箱",
          emailPassword: "邮箱密码",
          secretKey: "秘钥",
          name: "名字",
        };
      }
      this.isChinese = !this.isChinese;
    },
    //跳转页面
    handleJumpPage(){
      jumpPage(this.queryParams).then(response => {
        if (response.data === -1){
        }else {
          console.log(response.data);
          this.$message.info(Math.ceil((response.data+1)/this.queryParams.pageSize));
        }
      });
    },

    //检测
    handleCheckAccountActive(row) {
      const keyIds = row.keyId || this.keyIds
      checkAccountActive(keyIds).then(response => {
        this.$modal.msgSuccess("检测完毕");
      });
    },

    //同意添加好友
    handleConfirmAddFriend(row){
      const keyId = row.keyId
      confirmAddFriend(keyId).then(response => {

      })
    },
    modelOne() {
      this.queryParams = {
        ...this.queryParams, // 保留其他字段
        region: '',
        canLogin: '1',
        canAds: '1',
        isSell: '0',
        emailStatus: '2'
      };
    },
    modelTwo() {
      this.queryParams = {
        ...this.queryParams,
        region: '英文',
        canLogin: '1',
        canAds: '0',
        isSell: '0',
        emailStatus: '0'
      };
    },
    modelThree() {
      this.queryParams = {
        ...this.queryParams,
        region: '',
        canLogin: '',
        isSell: '',
        emailStatus: '',
        sellDate: '',
        friendNumber: '',
        note: ''
      };
    },
    handleGetEmail(row) {
      const keyId = row.keyId || this.keyIds
      getEmail(keyId).then(response => {
        this.$modal.alertSuccess(response.msg)
      })
    },

    handleUnlockEmail(row) {
      const keyIds = row.keyId || this.keyIds
      unlockEmail(keyIds).then(response => {

      })
    },

    handleUnlockWSVerify(row) {
      const keyIds = row.keyId || this.keyIds
      unlockWSVerify(keyIds).then(response => {

      })
    },

    handleLoginEmail(){
      const keyId = this.keyIds
      loginEmail(keyId)
    },

    handleDblClick(row){
      const id = row.id;

      // 跳转到目标页面（例如账号列表页）
      const routeUrl = this.$router.resolve({
        path: "/account/sell", // 你要跳转的页面路径
        query: {
          id: id
        }
      });

      window.open(routeUrl.href, "_blank");
    },

    handleLoginInPhone(row){
      const keyId = this.keyIds
      loginInPhone(keyId).then(response => {
        this.$modal.alertSuccess(response.msg)
      })
    }
  }
};
</script>

<style scoped>
.sell-dialog .el-dialog__body {
  padding: 0; /* 去掉默认内边距，避免双层滚动 */
}

.sell-dialog-body {
  max-height: 70vh; /* 固定内容区域最大高度，可根据需要调整 */
  overflow-y: auto; /* 超出滚动 */
  padding: 20px; /* 内容留白 */
}

.sell-item {
  background: #fafafa;
  border-radius: 8px;
  padding: 10px 15px;
  margin-bottom: 12px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.05);
}
</style>
