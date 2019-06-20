<#import "parts/common.ftl" as com>
<@com.page false>

    <div class="container">
        <div class="row">
            <div class="col-6">
                <div class="row">
                    <div class="col-12">
                        <div class="page-header">
                            <h3>${player.name}</h3>
                        </div>
                    </div>
                </div>
                <div class="row">
                    <div class="col-12">
                        <form action="/team/player/${player.id}" method="post">
                            <div class="form-group">
                                <label>Name</label>
                                <input class="form-control" disabled name="name" value="${player.name}">
                            </div>
                            <div class="form-group">
                                <label>Position</label>
                                <input class="form-control" type="text" name="position" value="${player.position}">
                            </div>
                            <div class="form-group">
                                <label>State</label>
                                <input class="form-control" type="text" name="state" value="${player.state}">
                            </div>
                            <div class="form-group">
                                <label>Salary in mln of $</label>
                                <input class="form-control" type="number" name="salary" value="${player.salary}" >
                            </div>
                            <input type="hidden" value="${_csrf.token}" name="_csrf">
                            <button class="btn btn-primary" type="submit">Save</button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>
</@com.page>