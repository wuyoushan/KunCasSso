/**
 *
 */
package com.sso.server.authencation;

import com.kunghsu.vo.SessionFilter;
import com.kunghsu.vo.SsoLoginInfoVo;
import com.sso.server.credential.BaseCredential;
import com.sso.server.utils.SsoUtil;
import org.jasig.cas.authentication.Credential;
import org.jasig.cas.authentication.HandlerResult;
import org.jasig.cas.authentication.PreventedException;

import java.security.GeneralSecurityException;

/**
 *
 * @project web-sso-cas
 * @description 用户名密码校验器
 * @version 1.0
 * @history 修订历史（历次修订内容、修订人、修订时间等）
 *
 */
public class UsernamePasswordAuthencationHandler extends BaseAbstractAuthencationHandler {

	boolean checkCaptchCode = true;//是否校验验证码

	public void setCheckCaptchCode(boolean checkCaptchCode) {
		this.checkCaptchCode = checkCaptchCode;
	}

	@Override
	protected HandlerResult doAuthentication(Credential credential) throws GeneralSecurityException, PreventedException {

		BaseCredential baseCredential = (BaseCredential)credential;
		//是否需要验证验证码
		if(this.checkCaptchCode){
//			this.checkCaptchCode(baseCredential);
			checkSliderVerificationCode(baseCredential);
		}

		//拿到sessionID
		String ssoSessionId = SessionFilter.getRequest().getSession().getId();

		//开始做登录操作
		//至于校验是否登录成功，可以查数据库或者调用用户中心来判断用户是否能登录

		//假如登录不成功，可以抛出异常
		//TODO
		System.out.println("username:" + baseCredential.getUserName());
		System.out.println("passWord:" + baseCredential.getPassWord());

		// 假如登录成功，继续往下执行
		SsoLoginInfoVo ssoLoginInfoVo = new SsoLoginInfoVo();
		ssoLoginInfoVo.setUsername(baseCredential.getUserName());
		ssoLoginInfoVo.setPassword(baseCredential.getPassWord());

		//注意，登录成功，要创建session
		SsoUtil.createSession(ssoSessionId, ssoLoginInfoVo, "1");
		SessionFilter.getRequest().getSession().setAttribute(SsoUtil.SSO_SESSION_KEY, ssoLoginInfoVo);

		//返回结果
		return createHandlerResult(credential, this.principalFactory.createPrincipal(ssoSessionId), null);
	}


}