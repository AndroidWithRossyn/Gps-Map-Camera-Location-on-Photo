/*
 ================================================================
	* App Name: Audio Book
	* Author: vocsyinfotech
 	* Description:  Main Custom Script File
 ================================================================
 */

 (function ($) {
    "use strict";
// Preloader
$(window).on('load', function () {
	$('[data-loader="dual-ring"]').fadeOut(); // will first fade out the loading animation
	$('.preloader').delay(333).fadeOut('slow'); // will fade out the white DIV that covers the website.
	$('body').delay(333);
});

// Header Sticky
$(window).on('scroll', function () {
	var stickytop = $('#header.sticky-top .bg-transparent');
	var stickytopslide = $('#header.sticky-top-slide');

	if ($(this).scrollTop() > 1) {
		stickytop.addClass("sticky-on-top");
		stickytop.find(".logo img").attr('src', stickytop.find('.logo img').data('sticky-logo'));
	} else {
		stickytop.removeClass("sticky-on-top");
		stickytop.find(".logo img").attr('src', stickytop.find('.logo img').data('default-logo'));
	}

	if ($(this).scrollTop() > 180) {
		stickytopslide.find(".primary-menu").addClass("sticky-on");
		stickytopslide.find(".logo img").attr('src', stickytopslide.find('.logo img').data('sticky-logo'));
	} else {
		stickytopslide.find(".primary-menu").removeClass("sticky-on");
		stickytopslide.find(".logo img").attr('src', stickytopslide.find('.logo img').data('default-logo'));
	}
});

// Sections Scroll
if ($("body").hasClass("side-header")) {
	$('.smooth-scroll').on('click', function () {
		event.preventDefault();
		var sectionTo = $(this).attr('href');
		$('html, body').stop().animate({
			scrollTop: $(sectionTo).offset().top}, 100, 'easeInOutExpo');
	});
} else {
	$('.smooth-scroll').on('click', function () {
		event.preventDefault();
		var sectionTo = $(this).attr('href');
		$('html, body').stop().animate({
			scrollTop: $(sectionTo).offset().top - 50}, 100, 'easeInOutExpo');
	});
}

// Mobile Menu
$('.navbar-toggler').on('click', function () {
  $(this).toggleClass('show');
});
$(".navbar-nav a").on('click', function () {
  $(".navbar-collapse, .navbar-toggler").removeClass("show");
});

// Overlay Menu & Side Open Menu
$('.navbar-side-open .collapse, .navbar-overlay .collapse').on('show.bs.collapse hide.bs.collapse', function (e) {
  e.preventDefault();
}),
$('.navbar-side-open [data-bs-toggle="collapse"], .navbar-overlay [data-bs-toggle="collapse"]').on('click', function (e) {
  e.preventDefault();
  $($(this).data('bs-target')).toggleClass('show');
})

/*---------------------------------
   Carousel (Owl Carousel)
----------------------------------- */
$(".owl-carousel").each(function (index) {
    var a = $(this);
	if ($("html").attr("dir") == 'ltr') {
		var rtlVal = true
	}else{
		var rtlVal = false
    }
	$(this).owlCarousel({
		rtl: rtlVal,
		autoplay: a.data('autoplay'),
		center: a.data('center'),
		autoplayTimeout: a.data('autoplaytimeout'),
		autoplayHoverPause: a.data('autoplayhoverpause'),
		loop: a.data('loop'),
		speed: a.data('speed'),
		nav: a.data('nav'),
		dots: a.data('dots'),
		autoHeight: a.data('autoheight'),
		autoWidth: a.data('autowidth'),
		margin: a.data('margin'),
		stagePadding: a.data('stagepadding'),
		slideBy: a.data('slideby'),
		lazyLoad: a.data('lazyload'),
		navText:['<i class="fa fa-chevron-left"></i>', '<i class="fa fa-chevron-right"></i>'],
		animateOut: a.data('animateout'),
		animateIn: a.data('animatein'),
		video: a.data('video'),
		items: a.data('items'),
		responsive:{
        0:{items: a.data('items-xs'),},
        576:{items: a.data('items-sm'),},
		768:{items: a.data('items-md'),},
        992:{items: a.data('items-lg'),}
        }
    });
});


/*------------------------------------
 Parallax Background
 -------------------------------------- */
$(".parallax").each(function () {
	$(this).parallaxie({
		speed: 0.5,
	});
});


/*------------------------
   tooltips
-------------------------- */
var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
  return new bootstrap.Tooltip(tooltipTriggerEl)
})

/*------------------------
 Scroll to top
 -------------------------- */
$(function () {
	$(window).on('scroll', function () {
		if ($(this).scrollTop() > 400) {
			$('#back-to-top').fadeIn();
		} else {
			$('#back-to-top').fadeOut();
		}
	});
});
$('#back-to-top').on("click", function () {
	$('html, body').animate({scrollTop: 0}, 'slow');
	return false;
});



})(jQuery)