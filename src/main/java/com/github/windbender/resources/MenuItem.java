package com.github.windbender.resources;

public class MenuItem {
	String title;
	String route;
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getRoute() {
		return route;
	}
	public void setRoute(String route) {
		this.route = route;
	}
	@Override
	public String toString() {
		return "MenuItem [title=" + title + ", route=" + route + "]";
	}
	public MenuItem(String title, String route) {
		super();
		this.title = title;
		this.route = route;
	}
	
	
}
