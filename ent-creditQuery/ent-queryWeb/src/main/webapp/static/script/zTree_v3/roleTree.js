    //数据右移动
    function addRole() {
        //移动方法
        //右移时Tree1 在第一个参数,Tree2第二个参数
        //表示Tree1移动致Tree2
        moveTreeNode(Tree1, Tree2);
        
    }
    
    //数据左移动
    function delRole() {
      //移动方法 参数相反
      moveTreeNode(Tree2, Tree1);
    }
    
    function moveTreeNode(zTree1, zTree2){
      var nodes = zTree1.getCheckedNodes(); //获取选中需要移动的数据
      for(var i=0;i<nodes.length;i++){      //把选中的数据从根开始一条一条往右添加
        var node = nodes[i];
        
        var strs={};      //新建一个JSON 格式数据,表示为一个节点,可以是根也可以是叶
        strs.id =node.id;
        strs.name=node.name;
        strs.parentId= node.parentId;
        strs.children = new Array();  //树节点里面有个 nodes 集合,用来存储父子之间的包涵关系
        
        //调用添加方法
        //strs : json 格式..拼装成树的一个节点
        //zTree2: 表示需要添加节点的树
        zTreeDataAddNode(strs,zTree2);
        
        //获取这个被添加的code 如果是右增加  用来把它从左边移除掉
        var parentId = strs.parentId;
        //使用递归移除 移除的时候从叶子开始找  和增加的时候刚好相反
        //参数1就是数组最后一个数据  
        //scode  : 上面截取的code 表示父亲节点 
        //zTree1 : 需要移除的树,在zTree1 里面移除此对象
        zTreeDataDelete(nodes[nodes.length-(i+1)],parentId,zTree1);

      }
      //把选中状态改为未选择
      zTree2.checkAllNodes(false);  
      zTree1.checkAllNodes(false);
      
      //刷新
      zTree2.refresh();
      zTree1.refresh();
    }
    
    //树数据移动方法
    function zTreeDataAddNode(strs,zTree2){
      var nodes = zTree2.transformToArray(zTree2.getNodes()); //获取需要添加数据的树下面所有节点数据
      //如果有多个数据需要遍历,找到strs 属于那个父亲节点下面的元素.然后把自己添加进去
      if(nodes.length > 0){
        
        //这个循环判断是否已经存在,true表示不存在可以添加,false存在不能添加
        var isadd=true;
        for(var j=0;j<nodes.length;j++){
            if(strs.id==nodes[j].id){
              isadd=false;
              break;
            }
        }
        
        //找到父亲节点
        var parentId = strs.parentId;
        var i=0;
        var flag =false;
        for(i ;i<nodes.length;i++){
          if(parentId ==nodes[i].id){
            flag = true;
            break;
          }
        }
        
        //同时满足两个条件就加进去,就是加到父亲节点下面去
        if(flag && isadd){
            var treeNode1=nodes[i];
            if(!treeNode1.children){
            	treeNode1.children = [];
            }
            treeNode1.children[treeNode1.children.length <=0 ? 0 : treeNode1.children.length++]=strs;
            
        //如果zTree2 里面找不到,也找不到父亲节点.就把自己作为一个根add进去
        }else if(isadd){
            zTree2.addNodes(null,0,strs);
          }
      }else{
            //树没任何数据时,第一个被加进来的元素
            zTree2.addNodes(null,strs);
      }
    }
    
    //数据移除
    function zTreeDataDelete(node,parentId,zTree1){
      if(node.isParent){  //判断是不是一个根节点,如果是一个根几点从叶子开始遍历寻找
        
      //如果是个根就检测nodes里面是否有数据
        if(node.children.length > 0){
          //取出来
          var fnodes  = node.children;
          for(var x = 0; x<fnodes.length; x++){
            //不是根节点.并且code 相当就是需要移除的元素
            if(!(fnodes[x].isParent) && fnodes[x].id == parentId){  
                //调用ztree 的移除方法,参数是一个节点json格式数据
                zTree1.removeNode(fnodes[x]);
                
                //如果当前这个节点又是个根节点.开始递归
            }else if(fnodes[x].isParent){ 
                zTreeDataDelete(fnodes[x],parentId,zTree1);
            }
          }
        }else{
            //如果是个根,但是下面的元素没有了.就把这个根移除掉
            zTree1.removeNode(node);
        }
      }else{
        //不是就直接移除
        zTree1.removeNode(node);
      }
    }
    //全局变量,树1 , 树2
    var Tree1, Tree2;

  //ztree 的设置.对于标准格式数据.不需要设置我注释的那部分.
  //那部分属于简单格式数据需要设置的...不清楚去看ztree demo
    var setting = {
      check: {
    	  enable: true,   //true / false 分别表示 显示 / 不显示 复选框或单选框
    	  autoCheckTrigger: true,   //true / false 分别表示 触发 / 不触发 事件回调函数
    	  chkStyle: "checkbox",   //勾选框类型(checkbox 或 radio）
    	  chkboxType: { "Y": "ps", "N": "ps" }   //勾选 checkbox 对于父子节点的关联关系
        },
      //isSimpleData: true,
      //treeNodeKey: "id",
      //treeNodeParentKey: "pId",
      showLine: true
      //root:{ 
      //  isRoot:true,
      //  nodes:[]
      //},
      
    };

    function roleManage(obj,id,type){
      $.ajaxSetup({  
          async : false  
      });  
      var datadAll,urlAll,datadUser,urlUser,submitUrl;
      if(type == 0){
        urlAll = obj.getAllMenu+id;
        urlUser=obj.getUserMenu+id;
        submitUrl = obj.menuRoleSubmit;
      }else if(type == 1){
        urlAll = obj.getAllButton+id;
        urlUser=obj.getUserButton+id;
        submitUrl = obj.buttonRoleSubmit;
      }else if(type == 2){
        urlAll = obj.allRole+id;
        urlUser=obj.userRole+id;
        submitUrl = obj.userIdAndRoleSubmit;
      }
      // 左侧树数据
      $.post(urlAll,function(data){
          datadAll = strToJson(data);
      })
      // 左侧树数据
      $.post(urlUser,function(data){       
          datadUser = strToJson(data);
      })
      //树1 数据 生成
      Tree1 = $.fn.zTree.init($("#srcTree"),setting, datadAll);  
      Tree1.expandAll(true);
      //树2 数据null
      Tree2 = $.fn.zTree.init($("#tarTree"),setting, datadUser);
      Tree2.expandAll(true);
      var flag1 = false,flag2 = false;
      $("#btnAllL").on("click",function(argument) {
        var t = $(this);
        if(flag1){
          flag1 = false;
          t.text("左侧全选");
        }else{
          flag1 = true;
          t.text("左侧全取消");
        }
        Tree1.checkAllNodes(flag1);
      });
      $("#btnAllR").on("click",function(argument) {
        var t = $(this);
        if(flag2){
          flag2 = false;
          t.text("右侧全选");
        }else{
          flag2 = true;
          t.text("右侧全取消");
        }
        Tree2.checkAllNodes(flag2);
      });
      $("#save").on("click",function(argument) {
        var idArr = [],nodeArr = Tree2.transformToArray(Tree2.getNodes());
        for(var i=0;i<nodeArr.length;i++){
            idArr.push(nodeArr[i].id);
        }
        var str = idArr.join(",");
        roleSubmitFun(submitUrl+id+","+str);
      });
  }

  function roleSubmitFun(url){
        ajaxFunc(url,function(result){
            console.log(result.code);
            console.log(result.msg);
            submitMsg(result);
        },function(result){
            layerMsg("error:"+JSON.stringify(result));
        });
  }