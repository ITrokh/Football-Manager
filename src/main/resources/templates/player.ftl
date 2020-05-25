<#import "parts/common.ftl" as com>
<@com.page false>

    <div class="container">
        <div class="row">
            <div class="col-6">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>${player.userName}</h3>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <form action="/teams/player/${player.id}" method="post">
                            <div class="form-group">
                                <label>Full Name</label>
                                <input class="form-control" disabled name="name" value="${player.name}">
                            </div>
                            <div class="form-group">
                                <label>Group</label>
                                <input class="form-control" disabled name="position" value="${player.group}">
                            </div>
                            <div class="form-group row">
                                <label class="col-sm-1 col-form-label">Total</label>
                                <div class="col-sm-6">
                                    <input type="total" disabled name="total" class="form-control" placeholder="Total" value="${player.total}"/>
                                </div>
                            </div>
                            <#list player.marks as mark>
                                <tr>
                                    <td><label>Lesson-${mark_index+1}<#if mark.getCreatedDate()??>(Date of appreciation: ${mark.getCreatedDate()}) </#if>
                                            <input name="${mark_index}"<#if mark.mark> disabled</#if> type="checkbox"${mark.mark?string("checked", "")}/></label></td>
                                </tr>
                            </#list>
                            <#--<div class="form-group">
                                <label>State</label>
                                <input class="form-control" type="text" name="state" value="${player.state}">
                            </div>
                            <div class="form-group">
                                <label>Salary in mln of $</label>
                                <input class="form-control" type="number" name="salary" value="${player.salary}" >
                            </div>-->
                            <input type="hidden" value="${_csrf.token}" name="_csrf">
                            <button class="btn btn-primary" type="submit">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@com.page>