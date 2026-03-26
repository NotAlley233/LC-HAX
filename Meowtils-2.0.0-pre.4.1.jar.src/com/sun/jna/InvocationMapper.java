package com.sun.jna;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public interface InvocationMapper {
  InvocationHandler getInvocationHandler(NativeLibrary paramNativeLibrary, Method paramMethod);
}


/* Location:              /Users/notalley/Downloads/Meowtils-2.0.0-pre.4.1.jar!/com/sun/jna/InvocationMapper.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       1.1.3
 */