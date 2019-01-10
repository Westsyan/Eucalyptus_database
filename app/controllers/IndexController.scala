package controllers

import play.api.mvc._

class IndexController  extends Controller {


  def home = Action{implicit request=>
    Ok(views.html.home.index())
  }


  def adminHome = Action{implicit request=>
    Ok(views.html.admin.index())

  }


  def loginBefore() = Action{implicit request=>
    Ok(views.html.admin.login())
  }

  def toAdmin = Action{implicit request=>
    if(request.session.get("id").isEmpty){
      Redirect(routes.IndexController.loginBefore())
    }else{
      Redirect(routes.IndexController.home())
    }
  }


}
