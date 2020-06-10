<#import "parts/common.ftl" as com>
<#include "parts/security.ftl">
<@com.page false>

    <div class="container">
        <div class="row">
            <div class="col-6">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>${student.userName}</h3>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <form action="/teams/student/${student.id}" method="post">
                            <div class="form-group">
                                <label>Full Name</label>
                                <input class="form-control" disabled name="name" value="${student.name}">
                            </div>
                            <div class="form-group">
                                <label>Group</label>
                                <input class="form-control" disabled name="position" value="${student.group}">
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-1 col-form-label">Total</label>
                                <div class="col-sm-6">
                                    <input type="total" disabled name="total" class="form-control" placeholder="Total" value="${student.total}"/>
                                </div>
                            </div>
                            <#if user.id==student.id || isTeamLeader>
                            <#list student.marks as mark>
                                <tr>
                                    <td><label>Lesson-${mark_index+1}<#if mark.getCreatedDate()??>(Date of appreciation: ${mark.getCreatedDate()}) </#if>
                                            <input name="${mark_index}"<#if mark.mark || !isTeamLeader> disabled</#if> type="checkbox"${mark.mark?string("checked", "")}/></label></td>
                                </tr>
                            </#list>
                            </#if>
                            <input type="hidden" value="${_csrf.token}" name="_csrf">
                            <button class="btn btn-primary" type="submit">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@com.page>