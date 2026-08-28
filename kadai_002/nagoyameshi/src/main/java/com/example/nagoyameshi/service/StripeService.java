package com.example.nagoyameshi.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.nagoyameshi.entity.User;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class StripeService {

	@Value("${stripe.api-key}")
	private String stripeApiKey;

	@Value("${stripe.price.id}")
	private String stripePriceId;

	// セッションを作成し、Stripeに必要な情報を返す
	public String createStripeSession(HttpServletRequest httpServletRequest, User user) {

		//商品準備
		Stripe.apiKey = stripeApiKey;
		String requestUrl = new String(httpServletRequest.getRequestURL());

		SessionCreateParams params = SessionCreateParams.builder()
				.addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
				.addLineItem(
						SessionCreateParams.LineItem.builder()
								.setPrice(stripePriceId)
								.setQuantity(1L)
								.build())
				//決済モード(サブスク)
				.setMode(SessionCreateParams.Mode.SUBSCRIPTION)
				//ユーザーの情報
				.putMetadata("userId", user.getId().toString())

				//URLの設定
				.setSuccessUrl(requestUrl.replace("/register/create", "/success?session_id={CHECKOUT_SESSION_ID}"))
				.setCancelUrl(requestUrl.replace("/create", ""))
				.build();

		try {
			Session session = Session.create(params);
			return session.getUrl();
		} catch (StripeException e) {
			e.printStackTrace();
			return "";
		}
	}

	// (ウェブホック)セッションから有料会員登録したユーザーのIDを抽出し、コントローラーへ返却する
	public Session processSessionCompleted(Event event) {
		// Stripeのイベントからオブジェクトを取り出す
		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();

		// ログ
		optionalStripeObject.ifPresentOrElse(stripeObject -> {
			System.out.println("有料会員登録のデータ解析処理が成功しました。");
			System.out.println("Stripe API Version: " + event.getApiVersion());
			System.out.println("stripe-java Version: " + Stripe.VERSION);
		},
				() -> {
					System.out.println("有料会員登録のデータ解析処理が失敗しました。");
					System.out.println("Stripe API Version: " + event.getApiVersion());
					System.out.println("stripe-java Version: " + Stripe.VERSION);
				});

		// データの取り出しに成功していれば、中身を解析してユーザーIDを返す
		if (optionalStripeObject.isPresent()) {
			return (Session) optionalStripeObject.get(); // 型変換してsessionを返す
		}

		return null;
	}

	//クレカ変更・解約を行うためのStripeカスタマーポータルURLを発行する
	public String createStripePortalSession(String stripeCustomerId, HttpServletRequest httpServletRequest) {
		// APIキーのセット
		Stripe.apiKey = stripeApiKey;

		// 現在のリクエストURL（マイページ等のURL）を取得
		String requestUrl = new String(httpServletRequest.getRequestURL());
		String returnUrl = requestUrl.replace("/subscription/portal", "/");

		// 解決策：頭にフォルダ名を直接書いて、決済用（checkout）ではなくポータル用だとJavaに明示する
		com.stripe.param.billingportal.SessionCreateParams params = com.stripe.param.billingportal.SessionCreateParams
				.builder()
				.setCustomer(stripeCustomerId)
				.setReturnUrl(returnUrl) // 戻り先URL
				.build();

		try {
			// Stripeサーバーと通信して、専用ポータルのセッションURLを発行
			com.stripe.model.billingportal.Session portalSession = com.stripe.model.billingportal.Session
					.create(params);
			return portalSession.getUrl(); // 発行されたURLを返却
		} catch (StripeException e) {
			e.printStackTrace();
			return ""; // 通信エラー時は空文字を返して安全にコントローラーへ受け渡す
		}
	}

	// (ウェブホック)有料会員解約
	public Subscription processSubscriptionDeleted(Event event) {

		Optional<StripeObject> optionalStripeObject = event.getDataObjectDeserializer().getObject();

		optionalStripeObject.ifPresentOrElse(stripeObject -> {
			System.out.println("有料会員解約のデータ解析処理が成功しました。");
		}, () -> {
			System.out.println("有料会員解約のデータ解析処理が失敗しました。");
		});

		if (optionalStripeObject.isPresent()) {
			return (Subscription) optionalStripeObject.get();
		}
		return null;

	}

}
