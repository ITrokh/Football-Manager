<#include "security.ftl">
<#macro login path isRegisterForm>
    <form action="${path}" method="post">
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">User Name :</label>
            <div class="col-sm-6">
                <input type="text" name="username" value="<#if user??>${user.username}</#if>"
                       class="form-control ${(usernameError??)?string('is-invalid', '')}"
                       placeholder="User name" />
                <#if usernameError??>
                    <div class="invalid-feedback">
                        ${usernameError}
                    </div>
                </#if>
            </div>
        </div>
        <div class="form-group row">
            <label class="col-sm-2 col-form-label">Password:</label>
            <div class="col-sm-6">
                <input type="password" name="password"
                       class="form-control ${(passwordError??)?string('is-invalid', '')}"
                       placeholder="Password" />
                <#if passwordError??>
                    <div class="invalid-feedback">
                        ${passwordError}
                    </div>
                </#if>
            </div>
        </div>
        <#if isRegisterForm>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Password Confirm:</label>
                <div class="col-sm-6">
                    <input type="password" name="passwordConfirm"
                           class="form-control ${(passwordConfirmError??)?string('is-invalid', '')}"
                           placeholder="Retype password" />
                    <#if password2Error??>
                        <div class="invalid-feedback">
                            ${passwordConfirmError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Your full name:</label>
                <div class="col-sm-6">
                    <input type="text" name="fullName"
                           class="form-control ${(fullNameError??)?string('is-invalid', '')}"
                           placeholder="Input your full name" />
                    <#if fullNameError??>
                        <div class="invalid-feedback">
                            ${fullNameError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <label class="col-sm-2 col-form-label">Email:</label>
                <div class="col-sm-6">
                    <input type="email" name="email" value="<#if user??>${user.email}</#if>"
                           class="form-control ${(emailError??)?string('is-invalid', '')}"
                           placeholder="some@some.com" />
                    <#if emailError??>
                        <div class="invalid-feedback">
                            ${emailError}
                        </div>
                    </#if>
                </div>
            </div>
            <div class="form-group row">
                <#if isAdmin>
                <label class="col-sm-2 col-form-label">Choose Role:</label>
                <div class="col-sm-6 form-check">
                <#list roles as role>
                    <div>
                        <input type="checkbox" class="form-check-input" name="roles" value="${role}" id="${role}"/>
                        <label class="form-check-label" for="${role}">${role}</label>
                    </div>
                </#list>
                </div>
                </#if>
            </div>
        </#if>
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <a href="/registration">Registration</a>
        <button class="btn btn-primary" type="submit"><#if isRegisterForm>Create<#else>Log In</#if></button>
    </form>
</#macro>

<#macro logout>
    <form action="/logout" method="post">
        <input type="hidden" name="_csrf" value="${_csrf.token}" />
        <button class="btn btn-primary" type="submit">
            <#if user??>Sign Out<#else> Log in </#if></button>
    </form>
</#macro>