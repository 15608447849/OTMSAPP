package ping.otmsapp.assemblys.fragments.base;

import java.util.LinkedList;

/**
 * Created by Leeping on 2018/4/4.
 * email: 793065165@qq.com
 * 基于linkedList实现的回退栈
 */

public class BackStack<E> {
    private LinkedList<E> ll = new LinkedList<E>();
    //入栈
    public void push(E e){
        ll.addFirst(e);
    }

    //查看栈顶元素但不移除
    public E peek(){
        return ll.getFirst();
    }

    //出栈
    public E pop(){
        return ll.removeFirst();
    }

    //判空
    public boolean empty(){
        return ll.isEmpty();
    }

    //打印栈元素
    public String toString(){
        return ll.toString();
    }
}
