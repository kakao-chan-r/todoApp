$(document).ready(function() {
    $.ajaxSetup({ cache: false }); // ajax cache 설정 : IE 에서 rest API 호출 시 time stamp를 통한 cache 방지
    fnTodoList(1);

    $('#addTodoBtn').on('click',function(){
        fnAddTodo();
    });
    $('#deleteTodo').on('click',function(){
        fnDeleteTodo();
    });
    $('#saveModal').on('click', function() {
        fnModifyTodo();
    });
    $('#delCheckBtn').on('click', function() {
        fnChkDelTodo();
    });
    $('.modal').on('show.bs.modal', function (e) {
        let modalId = $(e.relatedTarget).data('id');
        $('#modalId').val(modalId);
        $.ajax({
            type : 'GET',
            url : '/api/readTodo/' + modalId,
            dataType : 'json',
            contentType : 'application/json',
            success : function(response){
                fnModalGrid(response);
            },
            error : function(jqXHR, textStatus, errorThrown){
                alert('error : ' + jqXHR.responseText);
            }
        });
    });
    $('#taskSelect').click(function (e) {
        $('select').moveToListAndDelete('#taskSelect', '#refTaskSelect');
        e.preventDefault();
    });
    $('#refTaskSelect').click(function (e) {
        $('select').moveToListAndDelete('#refTaskSelect', '#taskSelect');
        e.preventDefault();
    });

    $('#todoText').on("keypress", function (e) {
        if (e.keyCode === 13) {
            fnAddTodo();
        }
    });
});

let curPage = 0;

function fnTodoList(currentPage){
    const todo = {
        currentPage : currentPage,
        perPageCnt : '5',
        displayPageNum : '3'
    }
    $.ajax({
        type : 'GET',
        url : '/api/todoList',
        dataType : 'json',
        data : todo,
        contentType : 'application/json',
        success : function(response){
            let todoList    = response.todoList;
            let pageConfig  = response.pageConfig;
            curPage = currentPage;
            $('#tableTbody').html('');
            $('.pagination').html('');
            fnTbodyGrid(todoList);
            fnPageNavGrid(pageConfig);
        },
        error : function(jqXHR, textStatus, errorThrown){
            alert('error : ' + jqXHR.responseText);
        }
    });
}

/**
 * create todo function
 * @param taskText : task contents
 */
function fnAddTodo(){
    let taskText = $('#todoText').val();
    if( taskText == ""){
        alert("내용을 입력해 주세요.");
        $('#todoText').focus();
        return;
    }
    if(taskText.length > 255){
        alert("내용은 255자가 넘을 수 없습니다.");
        $('#todoText').focus();
        return;
    }
    const addTodo = {
        task : taskText
    }
    $.ajax({
        type : 'POST',
        url : '/api/addTodo',
        data : JSON.stringify(addTodo),
        contentType : 'application/json',
        success : function(response){
            alert("등록되었습니다.");
            $('#todoText').val('');
            fnTodoList(1);
        },
        error : function(jqXHR, textStatus, errorThrown){
            alert('error : ' + jqXHR.responseText);
        }
    });
}
/**
 * modify(save) todo function
 * @param
 */
function fnModifyTodo(){
    let modalTaskText = $('#modalTaskText').val();
    if( modalTaskText == ""){
        alert("내용을 입력해 주세요.");
        $('#modalTaskText').focus();
        return;
    }
    if(modalTaskText.length > 255){
        alert("내용은 255자가 넘을 수 없습니다.");
        $('#modalTaskText').focus();
        return;
    }
    let selectIdList    = [];
    let selectRefIdList = [] ;

    $("#refTaskSelect option").each(function() { selectRefIdList.push($(this).val()); }); // 참조된 id list
    $("#taskSelect option").each(function() { selectIdList.push($(this).val()); }); // 참조되지 않은 id list
    const modifyTodo = {
        id :  $('#modalId').val(),
        task : modalTaskText,
        status : $('input:checkbox[id="modalStatus"]').is(":checked"),
        refIdList: selectRefIdList,
        idList : selectIdList
    }
    $.ajax({
        type : 'PUT',
        url : '/api/modifyTodo',
        data : JSON.stringify(modifyTodo),
        contentType : 'application/json',
        success : function(response){
            alert('수정되었습니다.');
            $('.modal').modal("hide");
            fnTodoList(curPage);
        },
        error : function(jqXHR, textStatus, errorThrown){
            alert('error : ' + jqXHR.responseText);
        }
    });
}
/**
 * delete todo function
 * @param id : target todo id
 */
function fnDeleteTodo(id){
    let deleteTaskText = ".task-text" + id;
    if(confirm($(deleteTaskText).html() + "을(를) 삭제하시겠습니까?")) {
        $.ajax({
            type : 'DELETE',
            url : '/api/deleteTodo/' + id,
            contentType : 'application/json',
            success : function(response){
                alert("삭제되었습니다.");
                fnTodoList(curPage);
            },
            error : function(jqXHR, textStatus, errorThrown){
                alert('error : ' + jqXHR.responseText);
            }
        });
    }
}

/**
 * checked todo delete
 */
function fnChkDelTodo(){
    let checkIdList = [] ;
    $('input:checkbox[id^="checkId"]').val();
    $('input[id^="checkId"]:checked').each(function() {
        if($(this).is(":disabled")){}
        else{
            checkIdList.push($(this).val());
        }
    });
    if(checkIdList.length == 0){
        alert('선택된 항목이 없습니다.');
        return;
    }
    if(confirm("선택된 항목을 삭제 하시겠습니까?")) {
        const deleteAllTodo = {
            idList : checkIdList
        }
        $.ajax({
            type : 'DELETE',
            url : '/api/deleteAll' ,
            contentType : 'application/json',
            data : JSON.stringify(deleteAllTodo),
            success : function(response){
                alert("삭제되었습니다.");
                fnTodoList(1);
            },
            error : function(jqXHR, textStatus, errorThrown){
                alert('error : ' + jqXHR.responseText);
            }
        });
    }
}

/**
 * List Grid function
 * @param response : List<todoList>
 */
function fnTbodyGrid(gridData){
    let tbodyHtml   = "";
    let tbodyRefHtml= "";
    if(gridData.length > 0){
        $(gridData).each(function(i,iValue){    // todo list 확인
            let tbodyId         = iValue.id;
            let tbodyTask       = iValue.id + ' ' + iValue.task;
            let tbodyCheckCss   = iValue.status === true ? 'checked disabled' : '';
            let tbodyRegDate    = fnFormatDate(iValue.regDate);
            let tbodyModDate    = fnFormatDate(iValue.modDate);
            let tbodyRefCnt     = iValue.refCnt;
            let tbodyStatusCss  = iValue.status === true ? 'style=\ntext-decoration:line-through;\n' : '';
            tbodyRefHtml         = "";

            if(tbodyRefCnt > 0){    // 참조된 리스트 존재
                let tbodyRefList    = iValue.refList;
                tbodyRefHtml        += '<br/>';
                $(tbodyRefList).each(function(j,jValue){
                    let tbodyRefId          = '@' + jValue.id;
                    let tbodyRefStatusCss   = jValue.status === true ? 'style=\ntext-decoration:line-through;\n' : '';
                    tbodyRefHtml += '<span ' + tbodyRefStatusCss + '>'+tbodyRefId+'</span>';
                });
            }

            tbodyHtml += '<tr>';
            tbodyHtml += '    <td>';
            tbodyHtml += '        <div class=\ncustom-control\n>';
            tbodyHtml += '            <input type=\ncheckbox\n class=\ncustom-control-input\n value=\n' + tbodyId + '\n id=\ncheckId'+ tbodyId +'\n '+ tbodyCheckCss +' >';
            tbodyHtml += '                <label class=\ncustom-control-label\n for=\ncheckId'+ tbodyId +'\n/>';
            tbodyHtml += '        </div>';
            tbodyHtml += '    </td>';
            tbodyHtml += '    <td class=\ntask-tr\n data-toggle=\nmodal\n data-target=\n.modal\n data-id=\n'+ tbodyId + '\n>';
            tbodyHtml += '        <span class=\ntask-text' + tbodyId + '\n '+ tbodyStatusCss +'>' + tbodyTask + '</span>';
            tbodyHtml +=          tbodyRefHtml;
            tbodyHtml += '    </td>';
            tbodyHtml += '    <td>' + tbodyRegDate + '</td>';
            tbodyHtml += '    <td>' + tbodyModDate + '</td>';
            tbodyHtml += '    <td><button type=\nbutton\n class=\nclose\n onClick=\nfnDeleteTodo(' + tbodyId + ')\n>&times;</button></td>'
            tbodyHtml += '</tr>';
        });
    }
    $('#tableTbody').html(tbodyHtml);
}

/**
 * page navigation grid
 * @param pageConfig data
 */
function fnPageNavGrid(gridData){
    let page            = gridData.page;
    let perPageNum      = gridData.perPageNum;
    let totalCount      = gridData.totalCount;
    let displayPageNum  = gridData.displayPageNum;
    let startPage       = gridData.startPage;
    let endPage         = gridData.endPage;
    let navHtml         = "";
    let prevHtml        = "";
    let nextHtml        = "";
    if(gridData.page > 1){
    }
    if (gridData.prev == true){
        prevHtml += '        <li class=\npage-item\n>';
        prevHtml += '            <a class=\npage-link\n onClick=\nfnTodoList('+startPage+'-1)\n style=\ncolor:#fff;\n> &lt; </a>';
        prevHtml += '        </li>';
    }
    if (gridData.next == true) {
        nextHtml += '        <li class=\npage-item\n>';
        nextHtml += '            <a class=\npage-link\n onClick=\nfnTodoList('+endPage+'+1)\n style=\ncolor:#fff;\n>&gt;</a>';
        nextHtml += '        </li>';
    }
    navHtml += prevHtml;
    for(let pageIdx = startPage; pageIdx <= endPage; pageIdx++){
        let navCss          = "";
        if(page == pageIdx){navCss = "style=color:#fff"}
        navHtml += '        <li class=\npage-item\n>';
        navHtml += '            <a class=\npage-link\n onClick=\nfnTodoList('+pageIdx+')\n ' + navCss+ '>' + pageIdx + '</a>';
        navHtml += '        </li>';
    }
    navHtml += nextHtml;
    $('.pagination').html(navHtml);
}

/**
 * modal select box grid
 * @param response : TodoDto
 */
function fnModalGrid(response){
    let possRefHtml = "";
    let refHtml     = "";
    $('#modalDate').html('');
    $('select[id=\ntaskSelect\n] option').remove();
    $('select[id=\nrefTaskSelect\n] option').remove();
    $('#modalTaskText').val(response.task);
    let modDate = response.modDate === null ? '' : ' 최종수정일:' + fnFormatDate(response.modDate);
    $('#modalDate').html('등록일:' + fnFormatDate(response.regDate) + modDate);

    if(response.possRefList.length>0){  // 자신과 자신을 참조하지 않는 todo list
        for(let i = 0; i < response.possRefList.length; i++){
            let possRefList = response.possRefList[i].id + ' ' + response.possRefList[i].task;
            possRefHtml += '<option value=\n' + response.possRefList[i].id + '\n>' + possRefList + '</option>';
        }
        $("#taskSelect").append(possRefHtml);
    }
    if(response.refList.length>0){  // 자신을 참조하는 todo list
        for(let j = 0; j < response.refList.length; j++){
            let refList = response.refList[j].id + ' ' + response.refList[j].task;modalTaskText
            refHtml += '<option value=\n' + response.refList[j].id + '\n>' + refList + '</option>';
        }
        $("#refTaskSelect").append(refHtml);
    }
    $("#modalStatus").prop('checked', response.status)
}

/**
 * String Date > yyyy-MM-dd type change function
 * @param date : String 형 date
 * @returns yyyy-MM-dd
 */
function fnFormatDate(date) {
    if(date != null){
        let d = new Date(date),
            month   = '' + (d.getMonth() + 1),
            day     = '' + d.getDate(),
            year    = d.getFullYear();
        if (month.length < 2)   month = '0' + month;
        if (day.length < 2)     day   = '0' + day;
        return [year, month, day].join('-');
    }else{
        return "";
    }
}