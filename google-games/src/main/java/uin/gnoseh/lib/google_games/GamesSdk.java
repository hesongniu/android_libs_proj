package uin.gnoseh.lib.google_games;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.games.GamesSignInClient;
import com.google.android.gms.games.PlayGames;
import com.google.android.gms.games.PlayGamesSdk;

/**
 * 登录流程：<br>
 * 1. init：初始化sdk会尝试登录<br>
 * 2. checkAuthenticate：登录成功返回playerId，至此登录流程完成；否则执行手动登录<br>
 * 3. signIn：手动登录，然后继续检查登录状态<br>
 */
public class GamesSdk {

    /**
     * 登录状态
     */
    private static boolean isAuthenticated = false;
    /**
     * 当前登录用户id
     */
    private static String currPlayerId = null;

    public static void init(@NonNull Context context) {
        PlayGamesSdk.initialize(context);
    }

    /**
     * 检查登录状态，传入回调操作
     */
    public static void checkAuthenticate(@NonNull Activity activity, Callback callback) {
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(activity);
        gamesSignInClient.isAuthenticated().addOnCompleteListener(isAuthenticatedTask -> {
            isAuthenticated =
                    (isAuthenticatedTask.isSuccessful() &&
                            isAuthenticatedTask.getResult().isAuthenticated());
            if (isAuthenticated) {
                //登录成功就应该可以回传playerId，否则认为没登录成功
                PlayGames.getPlayersClient(activity).getCurrentPlayer().addOnCompleteListener(mTask -> {
                            currPlayerId = mTask.getResult().getPlayerId();
                            callback.onSuccess(currPlayerId);
                        }
                ).addOnFailureListener(e -> callback.onFailed(e));
            } else {
                //没登录成功就需要显示图标走手动登录流程
                callback.onFailed(null);
            }
        }).addOnFailureListener(e -> callback.onFailed(e));
    }

    /**
     * 在发现未登录成功时，需要执行手动登录
     */
    public static void signIn(@NonNull Activity activity) {
        GamesSignInClient gamesSignInClient = PlayGames.getGamesSignInClient(activity);
        gamesSignInClient.signIn();
    }

}
