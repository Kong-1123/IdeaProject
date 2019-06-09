/**
 * 
 * watermark-dom is an GPL FreeSoftware.
 * watermark-dom 是采用GPL的自由软件。
 * 注意：基于本项目源码从事科研、论文、系统开发，"最好"在文中或系统中表明来自于本项目的内容和创意，否则所有贡献者可能会鄙视你和你的项目。 使用本项目源码请尊重程序员职业和劳动
 * 
 * author:saucxs
 * contact:saucxs@163.com
 * License:GPL
 */


(function(watermark){
    window.watermarkdivs = [];
    // 加载水印
    var loadMark = function(settings) {
        var defaultSettings={
            watermark_txt:"测试水印",
            watermark_x:20,//水印起始位置x轴坐标
            watermark_y:20,//水印起始位置Y轴坐标
            watermark_rows:20,//水印行数
            watermark_cols:20,//水印列数
            watermark_x_space:0,//水印x轴间隔
            watermark_y_space:0,//水印y轴间隔
            watermark_font:'微软雅黑',//水印字体
            watermark_color:'black',//水印字体颜色
            watermark_fontsize:'18px',//水印字体大小
            watermark_alpha:0.15,//水印透明度，要求设置在大于等于0.003
            watermark_width:150,//水印宽度
            watermark_height:100,//水印长度
            watermark_angle:30//水印倾斜度数
        };
        
        // 获取iframe子页面的document
        var documentSon = document.getElementById('mainIframe').contentWindow.document;
        documentSon.body.style.overflowX = "hidden";    

        //采用配置项替换默认值，作用类似jquery.extend
        if(arguments.length===1&&typeof arguments[0] ==="object")
        {
            var src=arguments[0]||{};
            for(key in src)
            {
                if(src[key]&&defaultSettings[key]&&src[key]===defaultSettings[key])
                    continue;
                else if(src[key])
                    defaultSettings[key]=src[key];
            }
        }

        if (window.watermarkdivs && window.watermarkdivs.length > 0) {
            documentSon.body.removeChild(documentSon.getElementById("otdivid"));
            window.watermarkdivs = [];
        }

        //获取页面最大宽度
        var page_width = Math.max(documentSon.body.scrollWidth,documentSon.body.clientWidth);
        //获取页面最大长度
        var page_height = Math.max(documentSon.body.scrollHeight,documentSon.body.clientHeight);

        // 创建文档碎片
        var oTemp = documentSon.createDocumentFragment();
        //创建水印外壳div
        var otdiv = documentSon.getElementById("otdivid");
        if(!otdiv){
            otdiv =documentSon.createElement('div');
            otdiv.id="otdivid";
            otdiv.style.pointerEvents = "none";
            otdiv.style.position = "absolute";
            otdiv.style.left = '0px';
            otdiv.style.top = '0px';
            documentSon.body.appendChild(otdiv);
        }

        //如果将水印列数设置为0，或水印列数设置过大，超过页面最大宽度，则重新计算水印列数和水印x轴间隔
        if (defaultSettings.watermark_cols == 0 || (parseInt(defaultSettings.watermark_x + defaultSettings.watermark_width *defaultSettings.watermark_cols + defaultSettings.watermark_x_space * (defaultSettings.watermark_cols - 1)) > page_width)) {
            defaultSettings.watermark_cols = parseInt((page_width - defaultSettings.watermark_x + defaultSettings.watermark_x_space) / (defaultSettings.watermark_width + defaultSettings.watermark_x_space));
            defaultSettings.watermark_x_space = parseInt((page_width - defaultSettings.watermark_x - defaultSettings.watermark_width * defaultSettings.watermark_cols) / (defaultSettings.watermark_cols - 1));
        }
        //如果将水印行数设置为0，或水印行数设置过大，超过页面最大长度，则重新计算水印行数和水印y轴间隔
        if (defaultSettings.watermark_rows == 0 || (parseInt(defaultSettings.watermark_y + defaultSettings.watermark_height * defaultSettings.watermark_rows + defaultSettings.watermark_y_space * (defaultSettings.watermark_rows - 1)) > page_height)) {
            defaultSettings.watermark_rows = parseInt((defaultSettings.watermark_y_space + page_height - defaultSettings.watermark_y) / (defaultSettings.watermark_height + defaultSettings.watermark_y_space));
            defaultSettings.watermark_y_space = parseInt(((page_height - defaultSettings.watermark_y) - defaultSettings.watermark_height * defaultSettings.watermark_rows) / (defaultSettings.watermark_rows - 1));
        }

        var x;
        var y;
        for (var i = 0; i < defaultSettings.watermark_rows; i++) {
            y = defaultSettings.watermark_y + (defaultSettings.watermark_y_space + defaultSettings.watermark_height) * i;
            for (var j = 0; j < defaultSettings.watermark_cols; j++) {
                x = defaultSettings.watermark_x + (defaultSettings.watermark_width + defaultSettings.watermark_x_space) * j;

                var mask_div = documentSon.createElement('div');
                var oText=documentSon.createTextNode(defaultSettings.watermark_txt);
                mask_div.appendChild(oText);
                mask_div.innerHTML = mask_div.innerHTML.replace(/,/g,"<br/>");

                // 设置水印相关属性start
                mask_div.id = 'mask_div' + i + j;
                //设置水印div倾斜显示
                mask_div.style.webkitTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.MozTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.msTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.OTransform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.transform = "rotate(-" + defaultSettings.watermark_angle + "deg)";
                mask_div.style.visibility = "";
                mask_div.style.position = "absolute";
                //选不中
                mask_div.style.left = x + 'px';
                mask_div.style.top = y + 'px';
                mask_div.style.overflow = "hidden";
                mask_div.style.zIndex = "9999";
                //mask_div.style.border="solid #eee 1px";
                mask_div.style.opacity = defaultSettings.watermark_alpha;
                mask_div.style.fontSize = defaultSettings.watermark_fontsize;
                mask_div.style.fontFamily = defaultSettings.watermark_font;
                mask_div.style.color = defaultSettings.watermark_color;
                mask_div.style.textAlign = "center";
                mask_div.style.width = defaultSettings.watermark_width + 'px';
                mask_div.style.height = defaultSettings.watermark_height + 'px';
                mask_div.style.display = "block";
                //设置水印相关属性end
                //附加到文档碎片中
                otdiv.appendChild(mask_div);

                window.watermarkdivs.push(otdiv); //控制页面大小变化时水印字体
            };
        };
        //一次性添加到document中
        documentSon.body.appendChild(oTemp);
    };

    watermark.init = function(settings) {
        window.onload = function() {
            loadMark(settings);
        };
        window.onresize = function() {
            loadMark(settings);
        };
    };

    watermark.load = function(settings){
        loadMark(settings);
    };
})(window.watermark = {} );