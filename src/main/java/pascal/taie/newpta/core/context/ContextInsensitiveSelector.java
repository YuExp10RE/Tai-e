/*
 * Tai-e: A Program Analysis Framework for Java
 *
 * Copyright (C) 2020 Tian Tan <tiantan@nju.edu.cn>
 * Copyright (C) 2020 Yue Li <yueli@nju.edu.cn>
 * All rights reserved.
 *
 * This software is designed for the "Static Program Analysis" course at
 * Nanjing University, and it supports a subset of Java features.
 * Tai-e is only for educational and academic purposes, and any form of
 * commercial use is disallowed.
 */

package pascal.taie.newpta.core.context;

import pascal.taie.java.classes.JMethod;
import pascal.taie.newpta.core.cs.CSCallSite;
import pascal.taie.newpta.core.cs.CSMethod;
import pascal.taie.newpta.core.cs.CSObj;
import pascal.taie.newpta.core.heap.Obj;

/**
 * Context-insensitive selector do not use any context elements, thus
 * the type of context elements is irrelevant, and can be safely ignored.
 */
@SuppressWarnings("rawtypes")
public class ContextInsensitiveSelector extends AbstractContextSelector {

    @Override
    public Context selectContext(CSCallSite callSite, JMethod callee) {
        return getDefaultContext();
    }

    @Override
    public Context selectContext(CSCallSite callSite, CSObj recv, JMethod callee) {
        return getDefaultContext();
    }

    @Override
    protected Context doSelectHeapContext(CSMethod method, Obj obj) {
        return getDefaultContext();
    }
}