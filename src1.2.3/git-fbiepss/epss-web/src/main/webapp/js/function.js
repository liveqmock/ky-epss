/*****************************************************\
*			公共Javascript脚本程序				*
\*****************************************************/

/*****************************************************\
*                   定义全局变量			       	  *
\*****************************************************/
var menu;
var debug = false;
var copyright = '';

/*****************************************************\
*      获得某一段文本的最大长度		              	    *
\*****************************************************/
function getMaxLength(inputString){
	var maxlength=0;
	for(var i=0;i<inputString.length;i++){
		if(escape(inputString.charAt(i)).length==6){
			maxlength=maxlength + 2;
		}else {
			maxlength=maxlength + 1;
		}
	}
	return maxlength;
}
/*****************************************************\
*      将无任何值的Select控件的长度设为固定长度       *
\*****************************************************/
function setNullSelectToBlank(obj,size){
	if(size==null){
			size=15;
	}
	var length= obj.options.length;
	if(length<=0){
		obj.options[0]=new Option("", "-10000", true, false);
		setSelectSize(obj,size);
	}
}
/*****************************************************\
*      设置Select控件最大长度         *
\*****************************************************/
function setSelectSize(obj,textsize){
	var size = obj.options.length;
	var maxlength = 0;
	var index = 0;
	if(size>0){
		for(var i=0;i<size;i++){
			var alength = getMaxLength(obj.options[i].text);
			if(alength>maxlength){
				maxlength=alength;
				index=i;
			}
		}

		var textlength = maxlength+2;
		if(textlength<textsize){
			var substr1 = "";
			for(var i = 0;i<(textsize-textlength);i++){
				substr1 = substr1 + " ";
			}
			obj.options[index].text = obj.options[index].text+substr1;
		}
	}else{
		setNullSelectToBlank(obj,textsize);
	}
}
/*
* 判断日期，格式：yyyy-mm-dd
* */
function checkDate(fld) {
    var mo, day, yr;
    var entry;
    //haiyu huang 2010-6-24 添加判断
    if (typeof(fld) == "object")
        entry = fld.value;
    else entry = fld;
	//alert(entry);
    var re = /\d{4}\b[-]\b\d{1,2}[-]\d{1,2}/;

    if (re.test(entry)) {
		//alert('12345');
        var delimChar = "-";
        var delim1 = entry.indexOf(delimChar);
        var delim2 = entry.lastIndexOf(delimChar);
        yr = parseInt(entry.substring(0, delim1), 10);
        mo = parseInt(entry.substring(delim1+1, delim2), 10);
        day= parseInt(entry.substring(delim2+1), 10);
        var testDate = new Date(yr, mo-1, day);
        //alert(testDate);
        if (testDate.getDate( ) == day) {
            if (testDate.getMonth( ) + 1 == mo) {
                if (testDate.getFullYear( ) == yr) {
                    return true;
                } else {
                    alert("输入年份有误！");
                }
            } else {
                alert("输入月份有误！");
            }
        } else {
            alert("输入日份有误!");
        }
    } else {
        alert("不正确日期格式. 日期输入格式如下 yyyy-mm-dd.");
    }
    return false;
}

function focusNext(form, elemName, evt) {
    evt = (evt) ? evt : event;
    var charCode = (evt.charCode) ? evt.charCode :
        ((evt.which) ? evt.which : evt.keyCode);

    if (charCode == 13 || charCode == 3) {
        form.elements[elemName].focus();
        return false;
    }
    return true;
}

function getCookieVal(offset) {
    var endstr = document.cookie.indexOf (";", offset);
    if (endstr == -1) {
        endstr = document.cookie.length;
    }
    return unescape(document.cookie.substring(offset, endstr));
}

function getCookie(name) {
    var arg = name + "=";
    var alen = arg.length;
    //alert(document.cookie);
    var clen = document.cookie.length;
    //alert('cookie.length='+clen);
    var i = 0;
    while (i < clen) {
        var j = i + alen;
        if (document.cookie.substring(i, j) == arg) {
            return getCookieVal(j);
        }
        i = document.cookie.indexOf(" ", i) + 1;
        if (i == 0) break;
    }
    return "";
}

function setCookie(name, value, expires, path, domain, secure) {
    document.cookie = name + "=" + escape (value) +
        ((expires)?"; expires=" + expires : "") +
        ((path)?"; path=" + path : "") +
        ((domain)?"; domain=" + domain : "") +
        ((secure)?"; secure" : "");
}

function id2obj(id){
     return document.getElementById(id);
}

function getInt(obj){
     obj = id2obj(obj);
	if(trimStr(obj.value)==''){
      	return 0;
	}else{
     	var i = parseInt(obj.value,10);
          if(isNaN(i)){
          	alert('值非法 !请重新录入!');
          	obj.focus();
               obj.select();
          	return 0;
          }else{
           	return i;
          }
	}
}
