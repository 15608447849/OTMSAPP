package ping.otmsapp.assemblys.fragments.base;


import ping.otmsapp.R;

/**
 * Created by user on 2017/1/11.
 */

public class FragmentAnim {
    public enum CoreAnim {
        button2top, /*由下到上动画 */
        top2button,/*  由上到下 */
        left2right,/* 从左到右动画 */
        right2left,/* 从右到左*/
        fade,/*渐变 */

    }
    public static int[] convertAnimations(CoreAnim coreAnim) {
        try {
            if (coreAnim == CoreAnim.left2right) {
                    return new int[] {R.anim.slide_in_right, R.anim.slide_out_right,R.anim.slide_in_right, R.anim.slide_out_right};
                }
            if (coreAnim == CoreAnim.right2left) {
                return new int[] {R.anim.slide_in_left, R.anim.slide_out_left,R.anim.slide_in_left, R.anim.slide_out_left};
            }
            if (coreAnim == CoreAnim.top2button){ //向下
                return new int[] {R.anim.push_in_down, R.anim.push_out_down,R.anim.push_in_down, R.anim.push_out_down};
            }
            if (coreAnim == CoreAnim.button2top){ //向上
                return new int[] {R.anim.push_in_up, R.anim.push_out_up,R.anim.push_in_up, R.anim.push_out_up};
            }

            if (coreAnim == CoreAnim.fade){ //渐变
                return new int[] {R.anim.alpha_in, R.anim.alpha_out,R.anim.alpha_in, R.anim.alpha_out};
            }

        } catch (Exception e) {

        }
        return null;
    }
}
