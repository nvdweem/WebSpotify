package com.vdweem.webspotify.action;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Actions;

import com.opensymphony.xwork2.Result;
import com.vdweem.webspotify.result.StdResult;

@Actions({
	@Action("Index"), @Action("/"), @Action("index.html")
})
public class IndexAction {

	public Result execute() {

		ThreadGroup root = Thread.currentThread().getThreadGroup().getParent();
		while (root.getParent() != null) {
		    root = root.getParent();
		}

		// Visit each thread group
		visit(root, 0);

		return new StdResult("_index.html");
	}

	void visit(ThreadGroup group, int level) {
	    // Get threads in `group'
	    int numThreads = group.activeCount();
	    Thread[] threads = new Thread[numThreads*2];
	    numThreads = group.enumerate(threads, false);

	    // Enumerate each thread in `group'
	    for (int i=0; i<numThreads; i++) {
	        // Get thread
	        Thread thread = threads[i];
	        System.out.println(thread);
	    }

	    // Get thread subgroups of `group'
	    int numGroups = group.activeGroupCount();
	    ThreadGroup[] groups = new ThreadGroup[numGroups*2];
	    numGroups = group.enumerate(groups, false);

	    // Recursively visit each subgroup
	    for (int i=0; i<numGroups; i++) {
	        visit(groups[i], level+1);
	    }
	}
}
