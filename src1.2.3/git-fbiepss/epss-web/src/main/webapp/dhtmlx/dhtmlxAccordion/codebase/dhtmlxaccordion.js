//v.3.5 build 120822

/*
 Copyright DHTMLX LTD. http://www.dhtmlx.com
 You allowed to use this component or parts of it under GPL terms
 To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
 */
function dhtmlXAccordionItem() {}
function dhtmlXAccordion(f, i) {
    if (window.dhtmlXContainer) {
        var d = this;
        this.skin = i != null ? i: typeof dhtmlx != "undefined" && typeof dhtmlx.skin == "string" ? dhtmlx.skin: "dhx_skyblue";
        if (f == document.body) {
            this._isAccFS = !0;
            document.body.className += " dhxacc_fullscreened";
            var g = document.createElement("DIV");
            g.className = "dhxcont_global_layout_area";
            f.appendChild(g);
            this.cont = new dhtmlXContainer(f);
            this.cont.setContent(g);
            f.adjustContent(f, 0);
            this.base = document.createElement("DIV");
            this.base.className = "dhx_acc_base_" + this.skin;
            this.base.style.overflow = "hidden";
            this.base.style.position = "absolute";
            this._adjustToFullScreen = function() {
                this.base.style.left = "2px";
                this.base.style.top = "2px";
                this.base.style.width = parseInt(g.childNodes[0].style.width) - 4 + "px";
                this.base.style.height = parseInt(g.childNodes[0].style.height) - 4 + "px"
            };
            this._adjustToFullScreen();
            g.childNodes[0].appendChild(this.base);
            this._resizeTM = null;
            this._resizeTMTime = 400;
            this._doOnResize = function() {
                window.clearTimeout(d._resizeTM);
                d._resizeTM = window.setTimeout(function() {
                        d._adjustAccordion()
                    },
                    d._resizeTMTime)
            };
            this._adjustAccordion = function() {
                document.body.adjustContent(document.body, 0);
                this._adjustToFullScreen();
                this.setSizes()
            };
            dhtmlxEvent(window, "resize", this._doOnResize)
        } else this.base = typeof f == "string" ? document.getElementById(f) : f,
            this.base.className = "dhx_acc_base_" + this.skin,
            this.base.innerHTML = "";
        this.w = this.base.offsetWidth;
        this.h = this.base.offsetHeight;
        this.skinParams = {
            dhx_blue: {
                cell_height: 24,
                cell_space: 1,
                content_offset: 1
            },
            dhx_skyblue: {
                cell_height: 27,
                cell_space: -1,
                content_offset: -1
            },
            dhx_black: {
                cell_height: 24,
                cell_space: 1,
                content_offset: 1
            },
            dhx_web: {
                cell_height: 26,
                cell_space: 9,
                content_offset: 0,
                cell_pading_max: 1,
                cell_pading_min: 0
            },
            dhx_terrace: {
                cell_height: 37,
                cell_space: -1,
                content_offset: -1
            }
        };
        this.sk = this.skinParams[this.skin];
        this.setSkinParameters = function(a, b) {
            isNaN(a) || (this.sk.cell_space = a);
            isNaN(b) || (this.sk.content_offset = b);
            this._reopenItem()
        };
        this.setSkin = function(a) {
            if (this.skinParams[a]) {
                this.skin = a;
                this.sk = this.skinParams[this.skin];
                this.base.className = "dhx_acc_base_" + this.skin + (this._r ? " dhx_acc_rtl": "");
                for (var b in this.idPull) this.idPull[b].skin = this.skin;
                this._reopenItem()
            }
        };
        this.idPull = {};
        this.opened = null;
        this.cells = function(a) {
            return this.idPull[a] == null ? null: this.idPull[a]
        };
        this.itemH = 90;
        this.multiMode = !1;
        this.enableMultiMode = function() {
            var a = 0,
                b;
            for (b in this.idPull) a++;
            if (a == 0) this.userOffset || (this.skinParams.dhx_skyblue.cell_space = 3),
                this.multiMode = !0;
            this.skin == "dhx_terrace" && (this.skinParams.dhx_terrace.cell_space = 12)
        };
        this.userOffset = !1;
        this.setOffset = function(a, b) {
            this.userOffset = !0;
            isNaN(a) || (this.skinParams[this.skin].cell_space = a);
            isNaN(b) || (this.skinParams[this.skin].content_offset = b);
            this.setSizes()
        };
        this.imagePath = "";
        this.setIconsPath = function(a) {
            this.imagePath = a
        };
        this.addItem = function(a, b) {
            if (this.multiMode) var e = this._lastVisible();
            var c = document.createElement("DIV");
            c.className = "dhx_acc_item";
            c.dir = "ltr";
            c._isAcc = !0;
            c.skin = this.skin;
            this.base.appendChild(c);
            if (this.multiMode) c.h = this.itemH;
            var h = document.createElement("DIV");
            h._idd = a;
            h.className = "dhx_acc_item_label";
            h.innerHTML = "<span>" + b + "</span><div class='dhx_acc_item_label_btmbrd'>&nbsp;</div><div class='dhx_acc_item_arrow'></div><div class='dhx_acc_hdr_line_l'></div><div class='dhx_acc_hdr_line_r'></div>";
            h.onselectstart = function(a) {
                a = a || event;
                a.returnValue = !1
            };
            h.onclick = function() {
                if (d.multiMode || !d.idPull[this._idd]._isActive) d.multiMode ? (d.idPull[this._idd]._isActive ? d.checkEvent("onBeforeActive") ? d.callEvent("onBeforeActive", [this._idd, "close"]) && d.closeItem(this._idd, "dhx_accord_outer_event") : d.closeItem(this._idd, "dhx_accord_outer_event") : d.checkEvent("onBeforeActive") ? d.callEvent("onBeforeActive", [this._idd, "open"]) && d.openItem(this._idd, "dhx_accord_outer_event") : d.openItem(this._idd, "dhx_accord_outer_event"), d._autoHeightEnabled && d.setSizes()) : d.checkEvent("onBeforeActive") ? d.callEvent("onBeforeActive", [this._idd, "open"]) && d.openItem(this._idd, "dhx_accord_outer_event") : d.openItem(this._idd, "dhx_accord_outer_event")
            };
            h.onmouseover = function() {
                this.className = "dhx_acc_item_label dhx_acc_item_lavel_hover"
            };
            h.onmouseout = function() {
                this.className = "dhx_acc_item_label"
            };
            c.appendChild(h);
            var f = document.createElement("DIV");
            f.className = "dhxcont_global_content_area";
            c.appendChild(f);
            var g = new dhtmlXContainer(c);
            g.setContent(f);
            this.skin == "dhx_terrace" && this._hideBorders === !0 && c._setPadding([0, -1, 2, 0]);
            c.adjustContent(c, this.sk.cell_height + this.sk.content_offset);
            c._id = a;
            this.idPull[a] = c;
            c.getId = function() {
                return this._id
            };
            c.setText = function(a) {
                d.setText(this._id, a)
            };
            c.getText = function() {
                return d.getText(this._id)
            };
            c.open = function() {
                d.openItem(this._id)
            };
            c.isOpened = function() {
                return d.isActive(this._id)
            };
            c.close = function() {
                d.closeItem(this._id)
            };
            c.setIcon = function(a) {
                d.setIcon(this._id, a)
            };
            c.clearIcon = function() {
                d.clearIcon(this._id)
            };
            c.dock = function() {
                d.dockItem(this._id)
            };
            c.undock = function() {
                d.undockItem(this._id)
            };
            c.show = function() {
                d.showItem(this._id)
            };
            c.hide = function() {
                d.hideItem(this._id)
            };
            c.setHeight = function(a) {
                d.setItemHeight(this._id, a)
            };
            c.moveOnTop = function() {
                d.moveOnTop(this._id)
            };
            c._doOnAttachMenu = function() {
                d._reopenItem()
            };
            c._doOnAttachToolbar = function() {
                d._reopenItem()
            };
            c._doOnAttachStatusBar = function() {
                d._reopenItem()
            };
            c._doOnFrameContentLoaded = function() {
                d.callEvent("onContentLoaded", [this])
            };
            if (this.multiMode && e != null) e._isActive == !0 ? (this.idPull[e._id].adjustContent(this.idPull[e._id], this.sk.cell_height + this.sk.content_offset, null, null, this.sk.cell_space), this.idPull[e._id].updateNestedObjects()) : this.idPull[e._id].style.height = this.sk.cell_height + this.sk.cell_space + "px",
                e = null;
            var i = this._enableOpenEffect;
            this._enableOpenEffect = !1;
            this.openItem(a);
            this._enableOpenEffect = i;
            this.multiMode ? this.setSizes() : this._defineLastItem();
            return c
        };
        this.openItem = function(a, b, e) {
            this.multiMode && this._checkAutoHeight();
            if (!this._openBuzy) if (this._enableOpenEffect && !e)(!this.multiMode || !this.idPull[a]._isActive) && this._openWithEffect(a, null, null, null, null, b);
            else if (this.multiMode) for (var c in this.idPull) {
                if (this.idPull[c]._isActive || c == a) this.idPull[c].style.height = this.idPull[c].h + "px",
                    this.idPull[c].childNodes[1].style.display = "",
                    this.skin == "dhx_web" && this.idPull[c]._setPadding(this.skinParams[this.skin].cell_pading_max, "dhxcont_acc_dhx_web"),
                    this.idPull[c].adjustContent(this.idPull[c], this.sk.cell_height + this.sk.content_offset, null, null, this.idPull[c] == this._lastVisible() && this.skin != "dhx_web" ? 0 : this.sk.cell_space),
                    this.idPull[c].updateNestedObjects(),
                    this.idPull[c]._isActive = !0,
                    this._updateArrows(),
                    b == "dhx_accord_outer_event" && c == a && this.callEvent("onActive", [a, !0])
            } else if (!a || !this.idPull[a]._isActive || e) {
                var d = 0;
                for (c in this.idPull) if (this.idPull[c].style.height = this.sk.cell_height + (this.idPull[c] != this._lastVisible() && c != a ? this.sk.cell_space: 0) + "px", c != a) this.idPull[c].childNodes[1].style.display = "none",
                    this.skin == "dhx_web" && this.idPull[c]._setPadding(this.skinParams[this.skin].cell_pading_min, ""),
                    this.idPull[c]._isActive = !1,
                    d += this.idPull[c].offsetHeight;
                d = this.base.offsetHeight - d;
                if (a) this.idPull[a].style.height = d + "px",
                    this.idPull[a].childNodes[1].style.display = "",
                    this.skin == "dhx_web" && this.idPull[a]._setPadding(this.skinParams[this.skin].cell_pading_max, "dhxcont_acc_dhx_web"),
                    this.idPull[a].adjustContent(this.idPull[a], this.sk.cell_height + this.sk.content_offset, null, null, this.idPull[a] == this._lastVisible() ? 0 : this.sk.cell_space),
                    this.idPull[a].updateNestedObjects(),
                    this.idPull[a]._isActive = !0,
                    b == "dhx_accord_outer_event" && this.callEvent("onActive", [a, !0]);
                this._updateArrows()
            }
        };
        this._lastVisible = function() {
            for (var a = null,
                     b = this.base.childNodes.length - 1; b >= 0; b--) ! this.base.childNodes[b]._isHidden && !a && (a = this.base.childNodes[b]);
            return a
        };
        this.closeItem = function(a, b) {
            if (this.idPull[a] != null && this.idPull[a]._isActive && !this._openBuzy) this._enableOpenEffect ? this._openWithEffect(this.multiMode ? a: null, null, null, null, null, b) : (this.idPull[a].style.height = this.sk.cell_height + (this.idPull[a] != this._lastVisible() ? this.sk.cell_space: 0) + "px", this.idPull[a].childNodes[1].style.display = "none", this.skin == "dhx_web" && this.idPull[a]._setPadding(this.skinParams[this.skin].cell_pading_min, ""), this.idPull[a]._isActive = !1, b == "dhx_accord_outer_event" && this.callEvent("onActive", [a, !1]), this._updateArrows())
        };
        this._updateArrows = function() {
            for (var a in this.idPull) {
                for (var b = this.idPull[a].childNodes[0], e = null, c = 0; c < b.childNodes.length; c++) String(b.childNodes[c].className).search("dhx_acc_item_arrow") != -1 && (e = b.childNodes[c]);
                if (e != null) e.className = "dhx_acc_item_arrow " + (this.idPull[a]._isActive ? "item_opened": "item_closed"),
                    e = null
            }
        };
        this.setText = function(a, b, e) {
            if (d.idPull[a] != null) {
                for (var c = d.idPull[a].childNodes[0], h = null, f = 0; f < c.childNodes.length; f++) c.childNodes[f].tagName != null && String(c.childNodes[f].tagName).toLowerCase() == "span" && (h = c.childNodes[f]);
                isNaN(e) ? h.innerHTML = b: (h.style.paddingLeft = e + "px", h.style.paddingRight = e + "px")
            }
        };
        this.getText = function(a) {
            if (d.idPull[a] != null) {
                for (var b = d.idPull[a].childNodes[0], e = null, c = 0; c < b.childNodes.length; c++) b.childNodes[c].tagName != null && String(b.childNodes[c].tagName).toLowerCase() == "span" && (e = b.childNodes[c]);
                return e.innerHTML
            }
        };
        this._initWindows = function(a) {
            if (window.dhtmlXWindows) {
                if (!this.dhxWins && (this.dhxWins = new dhtmlXWindows, this.dhxWins.setSkin(this.skin), this.dhxWins.setImagePath(this.imagePath), this.dhxWinsIdPrefix = "", !a)) return;
                var b = this.dhxWinsIdPrefix + a;
                if (this.dhxWins.window(b)) this.dhxWins.window(b).show();
                else {
                    var e = this,
                        c = this.dhxWins.createWindow(b, 20, 20, 320, 200);
                    c.setText(this.getText(a));
                    c.button("close").hide();
                    c.attachEvent("onClose",
                        function(a) {
                            a.hide()
                        });
                    c.addUserButton("dock", 99, this.dhxWins.i18n.dock, "dock");
                    c.button("dock").attachEvent("onClick",
                        function() {
                            e.cells(a).dock()
                        })
                }
            }
        };
        this.dockWindow = function(a) {
            if (this.idPull[a]._isUnDocked && this.dhxWins && this.dhxWins.window(this.dhxWinsIdPrefix + a)) this.dhxWins.window(this.dhxWinsIdPrefix + a).moveContentTo(this.idPull[a]),
                this.dhxWins.window(this.dhxWinsIdPrefix + a).close(),
                this.idPull[a]._isUnDocked = !1,
                this.showItem(a),
                this.callEvent("onDock", [a])
        };
        this.undockWindow = function(a) {
            if (!this.idPull[a]._isUnDocked) this._initWindows(a),
                this.idPull[a].moveContentTo(this.dhxWins.window(this.dhxWinsIdPrefix + a)),
                this.idPull[a]._isUnDocked = !0,
                this.hideItem(a),
                this.callEvent("onUnDock", [a])
        };
        this.setSizes = function() {
            this._reopenItem()
        };
        this.showItem = function(a) {
            if (this.idPull[a] != null && this.idPull[a]._isHidden) this.idPull[a]._isUnDocked ? this.dockItem(a) : (this.idPull[a].className = "dhx_acc_item", this.idPull[a]._isHidden = !1, this._defineLastItem(), this._reopenItem())
        };
        this.hideItem = function(a) {
            if (this.idPull[a] != null && !this.idPull[a]._isHidden) this.closeItem(a),
                this.idPull[a].className = "dhx_acc_item_hidden",
                this.idPull[a]._isHidden = !0,
                this._defineLastItem(),
                this._reopenItem()
        };
        this.isItemHidden = function(a) {
            return this.idPull[a] == null ? void 0 : this.idPull[a]._isHidden == !0
        };
        this._reopenItem = function() {
            var a = null,
                b;
            for (b in this.idPull) this.idPull[b]._isActive && !this.idPull[b]._isHidden && (a = b);
            this.openItem(a, null, !0)
        };
        this.forEachItem = function(a) {
            for (var b in this.idPull) a(this.idPull[b])
        };
        this._enableOpenEffect = !1;
        this._openStep = 10;
        this._openStepIncrement = 5;
        this._openStepTimeout = 10;
        this._openBuzy = !1;
        this.setEffect = function(a) {
            this._enableOpenEffect = a == !0 ? !0 : !1
        };
        this._openWithEffect = function(a, b, e, c, d, f) {
            if (this.multiMode) {
                if (!d) this._openBuzy = !0,
                    d = this._openStep,
                    this.idPull[a]._isActive ? (b = a, a = null, e = this.sk.cell_height + (this.idPull[b] != this._lastVisible() ? this.sk.cell_space: 0), this.idPull[b].childNodes[1].style.display = "") : (c = this.idPull[a].h, this.idPull[a].childNodes[1].style.display = "");
                var g = !1;
                if (a) {
                    var i = parseInt(this.idPull[a].style.height || 0) + d;
                    i > c && (i = c, g = !0);
                    this.idPull[a].style.height = i + "px"
                }
                if (b) i = parseInt(this.idPull[b].style.height) - d,
                    i < e && (i = e, g = !0),
                    this.idPull[b].style.height = i + "px";
                d += this._openStepIncrement;
                if (g) {
                    if (a) this.idPull[a].adjustContent(this.idPull[a], this.sk.cell_height + this.sk.content_offset, null, null, this.idPull[a] == this._lastVisible() ? 0 : this.sk.cell_space),
                        this.idPull[a].updateNestedObjects(),
                        this.idPull[a]._isActive = !0;
                    if (b) this.idPull[b].childNodes[1].style.display = "none",
                        this.idPull[b]._isActive = !1;
                    this._updateArrows();
                    this._openBuzy = !1;
                    a && f == "dhx_accord_outer_event" && this.callEvent("onActive", [a, !0]);
                    b && f == "dhx_accord_outer_event" && this.callEvent("onActive", [b, !1])
                } else {
                    var k = this;
                    window.setTimeout(function() {
                            k._openWithEffect(a, b, e, c, d, f)
                        },
                        this._openStepTimeout)
                }
            } else {
                if (!d && (this._openBuzy = !0, d = this._openStep, a)) this.idPull[a].childNodes[1].style.display = "";
                if (!b || !e || !c) {
                    var c = e = 0,
                        j;
                    for (j in this.idPull) {
                        var n = this.sk.cell_height + (this.idPull[j] != this._lastVisible() && j != a ? this.sk.cell_space: 0);
                        this.idPull[j]._isActive && a != j && (b = j, e = n);
                        j != a && (c += n)
                    }
                    c = this.base.offsetHeight - c
                }
                g = !1;
                if (a) {
                    var l = parseInt(this.idPull[a].style.height) + d;
                    l > c && (g = !0)
                }
                if (b) {
                    var m = parseInt(this.idPull[b].style.height) - d;
                    m < e && (g = !0)
                }
                d += this._openStepIncrement;
                g && (l = c, m = e);
                if (b) this.idPull[b].style.height = m + "px";
                if (a) this.idPull[a].style.height = l + "px";
                if (g) {
                    if (b) this.idPull[b].childNodes[1].style.display = "none",
                        this.idPull[b]._isActive = !1;
                    if (a) this.idPull[a].adjustContent(this.idPull[a], this.sk.cell_height + this.sk.content_offset, null, null, this.idPull[a] == this._lastVisible() ? 0 : this.sk.cell_space),
                        this.idPull[a].updateNestedObjects(),
                        this.idPull[a]._isActive = !0;
                    this._updateArrows();
                    this._openBuzy = !1;
                    f == "dhx_accord_outer_event" && a && this.callEvent("onActive", [a, !0])
                } else k = this,
                    window.setTimeout(function() {
                            k._openWithEffect(a, b, e, c, d, f)
                        },
                        this._openStepTimeout)
            }
        };
        this.setActive = function(a) {
            this.openItem(a)
        };
        this.isActive = function(a) {
            return this.idPull[a]._isActive === !0 ? !0 : !1
        };
        this.dockItem = function(a) {
            this.dockWindow(a)
        };
        this.undockItem = function(a) {
            this.undockWindow(a)
        };
        this.setItemHeight = function(a, b) {
            if (this.multiMode) {
                if (b == "*") this.idPull[a].h_auto = !0;
                else {
                    if (isNaN(b)) return;
                    this.idPull[a].h_auto = !1;
                    this.idPull[a].h = b
                }
                this._reopenItem()
            }
        };
        this._checkAutoHeight = function() {
            var a = this.base.offsetHeight;
            this._autoHeightEnabled = !1;
            var b = [],
                e;
            for (e in this.idPull) {
                if (!this._autoHeightEnabled && this.idPull[e].h_auto) this._autoHeightEnabled = !0;
                this.idPull[e].h_auto && this.idPull[e]._isActive ? b.push(e) : a = this.idPull[e]._isActive ? Math.max(0, a - this.idPull[e].h) : Math.max(0, a - this.idPull[e].offsetHeight)
            }
            if (b.length > 0) for (var c = Math.floor(a / b.length), d = 0; d < b.length; d++) d == b.length - 1 ? c = a: a -= c,
                this.idPull[b[d]].h = c
        };
        this.setIcon = function(a, b) {
            if (this.idPull[a] != null) {
                for (var e = this.idPull[a].childNodes[0], c = null, d = 0; d < e.childNodes.length; d++) e.childNodes[d].className == "dhx_acc_item_icon" && (c = e.childNodes[d]);
                if (c == null) c = document.createElement("IMG"),
                    c.className = "dhx_acc_item_icon",
                    e.insertBefore(c, e.childNodes[0]),
                    this.setText(a, null, 20);
                c.src = this.imagePath + b
            }
        };
        this.clearIcon = function(a) {
            if (this.idPull[a] != null) {
                for (var b = this.idPull[a].childNodes[0], d = null, c = 0; c < b.childNodes.length; c++) b.childNodes[c].className == "dhx_acc_item_icon" && (d = b.childNodes[c]);
                d != null && (b.removeChild(d), d = null, this.setText(a, null, 0))
            }
        };
        this.moveOnTop = function(a) {
            this.idPull[a] && !(this.base.childNodes.length <= 1) && (this.base.insertBefore(this.idPull[a], this.base.childNodes[0]), this.setSizes())
        };
        this._defineLastItem = function() {
            if (!this.multiMode) for (var a = !1,
                                          b = this.base.childNodes.length - 1; b >= 0; b--) this.base.childNodes[b].className.search("last_item") >= 0 ? this.base.childNodes[b]._isHidden || a ? this.base.childNodes[b].className = String(this.base.childNodes[b].className).replace(/last_item/gi, "") : a = !0 : !this.base.childNodes[b]._isHidden && !a && (this.base.childNodes[b].className += " last_item", a = !0)
        };
        this.removeItem = function(a) {
            var b = this.idPull[a],
                d = b.childNodes[0];
            d.onclick = null;
            d.onmouseover = null;
            d.onmouseout = null;
            d.onselectstart = null;
            d._idd = null;
            d.className = "";
            for (b._dhxContDestruct(); d.childNodes.length > 0;) d.removeChild(d.childNodes[0]);
            d.parentNode && d.parentNode.removeChild(d);
            for (d = null; b.childNodes.length > 0;) b.removeChild(b.childNodes[0]);
            b._dhxContDestruct = null;
            b._doOnAttachMenu = null;
            b._doOnAttachToolbar = null;
            b._doOnAttachStatusBar = null;
            b.clearIcon = null;
            b.close = null;
            b.dock = null;
            b.getId = null;
            b.getText = null;
            b.hide = null;
            b.isOpened = null;
            b.open = null;
            b.setHeight = null;
            b.setIcon = null;
            b.setText = null;
            b.show = null;
            b.undock = null;
            b.parentNode && b.parentNode.removeChild(b);
            b = null;
            this.idPull[a] = null;
            try {
                delete this.idPull[a]
            } catch(c) {}
        };
        this.unload = function() {
            for (var a in this.skinParams) {
                this.skinParams[a] = null;
                try {
                    delete this.skinParams[a]
                } catch(b) {}
            }
            this.skinParams = null;
            for (a in this.idPull) this.removeItem(a);
            this.userOffset = this.unload = this.undockWindowunload = this.undockWindow = this.undockItem = this.w = this.skin = this.showItem = this.setText = this.setSkinParameters = this.setSkin = this.setSizes = this.setOffset = this.setItemHeight = this.setIconsPath = this.setIcon = this.setEffect = this.setActive = this.removeItem = this.openItem = this.multiMode = this.itemH = this.isActive = this.imagePath = this.hideItem = this.h = this.getText = this.forEachItem = this.eventCatcher = this.enableMultiMode = this.dockWindow = this.dockItem = this.detachEvent = this.closeItem = this.clearIcon = this.checkEvent = this.cells = this.callEvent = this.attachEvent = this.addItem = this._updateArrows = this._reopenItem = this._lastVisible = this._initWindows = this.sk = this.idPull = null;
            if (this._isAccFS == !0) _isIE ? window.detachEvent("onresize", this._doOnResize) : window.removeEventListener("resize", this._doOnResize, !1),
                this._resizeTMTime = this._resizeTM = this._adjustToFullScreen = this._adjustAccordion = this._doOnResize = this._isAccFS = null,
                document.body.className = String(document.body.className).replace("dhxacc_fullscreened", ""),
                this.cont.obj._dhxContDestruct(),
                this.cont.dhxcont.parentNode && this.cont.dhxcont.parentNode.removeChild(this.cont.dhxcont),
                this.cont.dhxcont = null,
                this.cont = this.cont.setContent = null;
            if (this.dhxWins) this.dhxWins.unload(),
                this.dhxWins = null;
            this.base.className = "";
            this.base = null;
            for (a in this) try {
                delete this[a]
            } catch(d) {}
        };
        this._initWindows();
        dhtmlxEventable(this);
        return this
    } else alert(this.i18n.dhxcontalert)
}
dhtmlXAccordion.prototype.i18n = {
    dhxcontalert: "dhtmlxcontainer.js is missed on the page"
}; (function() {
    dhtmlx.extend_api("dhtmlXAccordion", {
            _init: function(f) {
                return [f.parent, f.skin]
            },
            icon_path: "setIconsPath",
            items: "_items",
            effect: "setEffect",
            multi_mode: "enableMultiMode"
        },
        {
            _items: function(f) {
                for (var i = [], d = [], g = 0; g < f.length; g++) {
                    var a = f[g];
                    this.addItem(a.id, a.text);
                    a.img && this.cells(a.id).setIcon(a.img);
                    a.height && this.cells(a.id).setHeight(a.height);
                    if (a.open === !0) i[i.length] = a.id;
                    if (a.open === !1) d[d.length] = a.id
                }
                for (var b = 0; b < i.length; b++) this.cells(i[b]).open();
                for (b = 0; b < d.length; b++) this.cells(d[b]).close()
            }
        })
})();
dhtmlXAccordion.prototype.loadJSON = function(f) {
    var i = {
            skin: "setSkin",
            icons_path: "setIconsPath",
            multi_mode: "enableMultiMode",
            effect: "setEffect"
        },
        d;
    for (d in i) if (typeof f[d] != "undefined") this[i[d]](f[d]);
    for (var g = null,
             a = 0; a < f.cells.length; a++) if (this.addItem(f.cells[a].id, f.cells[a].text), typeof f.cells[a].icon != "undefined" && this.setIcon(f.cells[a].id, f.cells[a].icon), typeof f.cells[a].height != "undefined" && this.setItemHeight(f.cells[a].id, f.cells[a].height), typeof f.cells[a].open != "undefined") g = f.cells[a].id;
    g != null && this.openItem(g)
};
dhtmlXAccordion.prototype.loadXML = function(f, i) {
    var d = this;
    this.callEvent("onXLS", []);
    dhtmlxAjax.get(f,
        function(f) {
            var a = f.xmlDoc.responseXML.getElementsByTagName("accordion")[0],
                b = {
                    0 : !1,
                    "true": !0,
                    1 : !0,
                    y: !0,
                    yes: !0
                },
                e = {
                    cells: []
                };
            if (a.getAttribute("skin") != null) e.skin = a.getAttribute("skin");
            if (a.getAttribute("iconsPath") != null) e.icons_path = a.getAttribute("iconsPath");
            if (a.getAttribute("mode") == "multi") e.multi_mode = !0;
            if (b[a.getAttribute("openEffect") || 0]) e.effect = !0;
            for (var c = null,
                     h = 0; h < a.childNodes.length; h++) if (typeof a.childNodes[h].tagName != "indefined" && String(a.childNodes[h].tagName).toLowerCase() == "cell") {
                var o = a.childNodes[h].getAttribute("id") || d._genStr(12),
                    p = a.childNodes[h].firstChild.nodeValue || "";
                e.cells.push({
                    id: o,
                    text: p
                });
                if (a.childNodes[h].getAttribute("icon") != null) e.cells[e.cells.length - 1].icon = a.childNodes[h].getAttribute("icon");
                if (a.childNodes[h].getAttribute("height") != null) e.cells[e.cells.length - 1].height = a.childNodes[h].getAttribute("height");
                if (b[a.childNodes[h].getAttribute("open") || 0]) e.cells[e.cells.length - 1].open = !0
            }
            d.loadJSON(e);
            d.callEvent("onXLE", []);
            typeof i == "function" && i();
            d = i = null
        })
};

//v.3.5 build 120822

/*
 Copyright DHTMLX LTD. http://www.dhtmlx.com
 You allowed to use this component or parts of it under GPL terms
 To use it on other terms or get Professional edition of the component please contact us at sales@dhtmlx.com
 */
