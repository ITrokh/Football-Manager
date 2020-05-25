<#import "parts/common.ftl" as com>
<#include "parts/security.ftl">
<@com.page false>
    ${message?ifExists}
    <form method="post">
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">Username:</label>
            <div class="col-sm-6">
                <input type="text" name="username" class="form-control" placeholder="Username" value="${username}" />
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" name="password" class="form-control" placeholder="Password" />
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">Email:</label>
            <div class="col-sm-6">
                <input type="email" name="email" class="form-control" placeholder="some@some.com" value="${email!''}" />
            </div>
        </div>
        <#if isStudent>
        <div class="form-group row">
            <label class="col-sm-1 col-form-label">Total</label>
            <div class="col-sm-6">
                <input type="total" disabled name="total" class="form-control" placeholder="Total" value="${student.total}"/>
            </div>
        </div>
        <#list student.marks as mark>
            <tr>
                <td><label>Lesson-${mark_index+1}<#if mark.getCreatedDate()??>(Date of appreciation: ${mark.getCreatedDate()}) </#if>
                        <input name="${mark_index}" disabled type="checkbox"${mark.mark?string("checked", "")}/></label></td>
            </tr>
        </#list>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">Save</button>
    </form>
</@com.page>

