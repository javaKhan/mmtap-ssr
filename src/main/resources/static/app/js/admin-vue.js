// header
Vue.component('admin-header', {
    template: '#admin-header',
    props: {
        title: [String],
        system_list: [Array],
        user_menus: [Array]
    },
    data: function () {
        return {
            search_text: ''
        }
    },
    methods: {
        //将事件提交给父组件
        sys_switch: function (sys) {
            this.title = sys.title;
            this.$emit('click_sys_switch', sys)
        },
        search: function () {
            this.$emit('click_search', this.search_text)
        }
    }
})
//main
Vue.component('admin-main', {
    template: '#admin-main',
    props: {
        user: [Object],
        user_menus: [Array],
        menus: [Array],
    },
    methods: {
        //将事件提交给父组件
        open_tab: function (title, url) {
            this.$emit('click_open_tab', title, url);
        }
    }
})

//sidebar
Vue.component('admin-sidebar', {
    template: '#admin-sidebar',
    props: {
        user: [Object],
        user_menus: [Array],
        menus: [Array]
    },
    methods: {
        //将事件提交给父组件
        open_tab: function (menu) {
            this.$emit('open_tab', menu.title, menu.url);
        }
    }
})

var data = {
    system_skin: 'skin-green',
    //header
    system_title: '知识产权信息管理',
    //system_list.name对应主题名称
    system_list: [
        {id: '1', skin: 'skin-green', title: '知识产权信息系统', icon: 'zmdi-shield-security'},
    ],
    //main
    user_menus: [
        // {title: '个人资料', icon: 'zmdi-account', url: '', isOpenTab: true},
        // {title: '隐私管理', icon: 'zmdi-face', url: '', isOpenTab: true},
        {title: '退出登录', icon: 'zmdi-run', url: 'logout', isOpenTab: false}
    ],
    user: {
        avater: 'app/img/avatar.jpg',
        name: 'bloom'
    },
    menus: [
        {title: '首页', icon: 'zmdi-home', url: 'home', isOpenTab: true},
        {
            title: '系统组织管理', icon: 'zmdi-accounts-list', children: [
            {title: '系统管理', icon: 'zmdi-account', url: 'crud.html', isOpenTab: true}
        ]
        },
        {
            title: ' 角色用户管理', icon: 'zmdi-accounts', children: [
            {title: '用户管理', icon: '', url: 'page/sys/user/table.html', isOpenTab: true},
            {title: '角色管理', icon: '', url: 'page/sys/role/table.html', isOpenTab: true}
        ]
        },
        {
            title: ' 权限资源管理', icon: 'zmdi-lock-outline', children: [
            {title: '权限管理', icon: '', url: 'page/sys/menu/menus.html', isOpenTab: true},
        ]
        },
        {
            title: '其他数据管理', icon: 'zmdi-more', children: [
            {title: '百度', icon: 'zmdi-lock-outline', url: 'https://www.baidu.com/', isOpenTab: true},
        ]
        }
    ]
}
//后期此处需要与用户关联上
var util = {
    verifyResources: function (systemList) {
        var system_id = $.cookie('bloom-system-id');
        if (!system_id) {
            return false;
        }
        for (var i = 0; i < systemList.length; i++) {
            if (system_id == systemList[i].id) {
                return true;
            }
        }
        return false;
    },
    //获取菜单列表
    getMenus: function (id, fc) {
        iGet('/api/users/current/menu', function (data) {
            var datas = data;
            var result = [];
            datas.forEach(function (obj) {
                if (obj.id == id) {
                    // console.log(obj)
                    result = obj.children;
                    // console.log(result)
                    if (result) {
                        result.forEach(function (item) {
                            item.title = item.name;
                            item.isOpenTab = true;
                            var children = item.children;
                            if (children) {
                                children.forEach(function (item) {
                                    item.title = item.name;
                                    item.isOpenTab = true;
                                });
                            }
                        })
                    } else {
                        result = [];
                    }
                }
            })
            // console.log(result);
            fc(result);
        });
    },
    //获取系统列表
    getSystemList: function (fc) {
        iGet('/api/users/current/menu', function (data) {
            var datas = data;
            datas.forEach(function (obj) {
                obj.title = obj.name;
                obj.name = obj.skin;
            })
            fc(datas);
        })
    },
    getCurrentUser: function (fc) {
        iGet('/api/users/current', function (userName) {
            // console.log('userName',userName);
            fc(userName);
        })
    },
    initData: function (that) {
        var _this = this;
        this.getSystemList(function (data) {
            that.system_list = data;
            var system_id = undefined;
            if (_this.verifyResources(that.system_list)) {
                that.system_skin = $.cookie('bloom-skin-name');
                that.system_title = $.cookie('bloom-system-title');
                system_id = $.cookie('bloom-system-id');
            } else {
                that.system_skin = that.system_list[0].skin;
                that.system_title = that.system_list[0].title;
                system_id = that.system_list[0].id;
            }
            $('title').text(that.system_title);

            _this.getMenus(system_id, function (data) {
                that.menus = data;
            })
        })

        this.getCurrentUser(function (userName) {
            that.user.name = userName;
        })
    }
}

var vue = new Vue({
    el: '#app',
    data: data,
    created: function () {
        util.initData(this);
    },
    methods: {
        sys_switch: function (sys) {// 切换系统
            $.cookie('bloom-skin-name', sys.skin);
            $.cookie('bloom-system-title', sys.title);
            $.cookie('bloom-system-id', sys.id);
            this.system_title = sys.title;
            this.system_skin = sys.skin;
            $('title').text(sys.title);
            var self = this;
            util.getMenus(sys.id, function (data) {
                self.menus = data;
            })
        },
        search: function (q) {
            console.log(q);
        },
        open_tab: function (title, url) {
            // console.log(title, url)
            Tab.addTab(title, url)
        }
    }
})