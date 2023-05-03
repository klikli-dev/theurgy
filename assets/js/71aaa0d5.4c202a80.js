"use strict";(self.webpackChunktheurgy_documentation=self.webpackChunktheurgy_documentation||[]).push([[894],{3905:(e,t,n)=>{n.d(t,{Zo:()=>s,kt:()=>g});var r=n(7294);function i(e,t,n){return t in e?Object.defineProperty(e,t,{value:n,enumerable:!0,configurable:!0,writable:!0}):e[t]=n,e}function a(e,t){var n=Object.keys(e);if(Object.getOwnPropertySymbols){var r=Object.getOwnPropertySymbols(e);t&&(r=r.filter((function(t){return Object.getOwnPropertyDescriptor(e,t).enumerable}))),n.push.apply(n,r)}return n}function o(e){for(var t=1;t<arguments.length;t++){var n=null!=arguments[t]?arguments[t]:{};t%2?a(Object(n),!0).forEach((function(t){i(e,t,n[t])})):Object.getOwnPropertyDescriptors?Object.defineProperties(e,Object.getOwnPropertyDescriptors(n)):a(Object(n)).forEach((function(t){Object.defineProperty(e,t,Object.getOwnPropertyDescriptor(n,t))}))}return e}function l(e,t){if(null==e)return{};var n,r,i=function(e,t){if(null==e)return{};var n,r,i={},a=Object.keys(e);for(r=0;r<a.length;r++)n=a[r],t.indexOf(n)>=0||(i[n]=e[n]);return i}(e,t);if(Object.getOwnPropertySymbols){var a=Object.getOwnPropertySymbols(e);for(r=0;r<a.length;r++)n=a[r],t.indexOf(n)>=0||Object.prototype.propertyIsEnumerable.call(e,n)&&(i[n]=e[n])}return i}var p=r.createContext({}),c=function(e){var t=r.useContext(p),n=t;return e&&(n="function"==typeof e?e(t):o(o({},t),e)),n},s=function(e){var t=c(e.components);return r.createElement(p.Provider,{value:t},e.children)},u="mdxType",m={inlineCode:"code",wrapper:function(e){var t=e.children;return r.createElement(r.Fragment,{},t)}},d=r.forwardRef((function(e,t){var n=e.components,i=e.mdxType,a=e.originalType,p=e.parentName,s=l(e,["components","mdxType","originalType","parentName"]),u=c(n),d=i,g=u["".concat(p,".").concat(d)]||u[d]||m[d]||a;return n?r.createElement(g,o(o({ref:t},s),{},{components:n})):r.createElement(g,o({ref:t},s))}));function g(e,t){var n=arguments,i=t&&t.mdxType;if("string"==typeof e||i){var a=n.length,o=new Array(a);o[0]=d;var l={};for(var p in t)hasOwnProperty.call(t,p)&&(l[p]=t[p]);l.originalType=e,l[u]="string"==typeof e?e:i,o[1]=l;for(var c=2;c<a;c++)o[c]=n[c];return r.createElement.apply(null,o)}return r.createElement.apply(null,n)}d.displayName="MDXCreateElement"},6248:(e,t,n)=>{n.r(t),n.d(t,{assets:()=>p,contentTitle:()=>o,default:()=>m,frontMatter:()=>a,metadata:()=>l,toc:()=>c});var r=n(7462),i=(n(7294),n(3905));const a={sidebar_position:20},o="Distillation Recipes",l={unversionedId:"recipe_types/distillation",id:"recipe_types/distillation",title:"Distillation Recipes",description:"Properties",source:"@site/docs/recipe_types/distillation.md",sourceDirName:"recipe_types",slug:"/recipe_types/distillation",permalink:"/theurgy/recipe_types/distillation",draft:!1,editUrl:"https://github.com/klikli-dev/theurgy/tree/documentation/docs/recipe_types/distillation.md",tags:[],version:"current",sidebarPosition:20,frontMatter:{sidebar_position:20},sidebar:"tutorialSidebar",previous:{title:"Calcination Recipes",permalink:"/theurgy/recipe_types/calcination"},next:{title:"Accumulation Recipes",permalink:"/theurgy/recipe_types/accumulation"}},p={},c=[{value:"Properties",id:"properties",level:2},{value:"Example Recipe",id:"example-recipe",level:2}],s={toc:c},u="wrapper";function m(e){let{components:t,...n}=e;return(0,i.kt)(u,(0,r.Z)({},s,n,{components:t,mdxType:"MDXLayout"}),(0,i.kt)("h1",{id:"distillation-recipes"},"Distillation Recipes"),(0,i.kt)("h2",{id:"properties"},"Properties"),(0,i.kt)("ul",null,(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"type")," - ",(0,i.kt)("strong",{parentName:"li"},"ResourceLocation"),", the recipe type. Must be ",(0,i.kt)("inlineCode",{parentName:"li"},"theurgy:distillation"),"."),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"distillation_time")," - ",(0,i.kt)("strong",{parentName:"li"},"Integer"),", The number of ticks the distillation process takes."),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"ingredient")," - ",(0,i.kt)("strong",{parentName:"li"},"Forge Ingredient"),", The item to be distilled.",(0,i.kt)("ul",{parentName:"li"},(0,i.kt)("li",{parentName:"ul"},"the item",(0,i.kt)("ul",{parentName:"li"},(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"tag")," ",(0,i.kt)("strong",{parentName:"li"},"Tag ResourceLocation"),", the tag accepted as input."),(0,i.kt)("li",{parentName:"ul"},"or ",(0,i.kt)("inlineCode",{parentName:"li"},"item")," ",(0,i.kt)("strong",{parentName:"li"},"Item ResourceLocation"),", the item accepted as input."))))),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"ingredient_count")," - ",(0,i.kt)("strong",{parentName:"li"},"Integer"),", The amount of input ingredient required for the recipe. Defaults to 1 if not specified."),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"result")," ",(0,i.kt)("strong",{parentName:"li"},"ItemStack")," representing the result of the distillation process.",(0,i.kt)("ul",{parentName:"li"},(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"count")," ",(0,i.kt)("strong",{parentName:"li"},"Integer"),", the output count."),(0,i.kt)("li",{parentName:"ul"},(0,i.kt)("inlineCode",{parentName:"li"},"item")," ",(0,i.kt)("strong",{parentName:"li"},"Item ResourceLocation"),", the output item.")))),(0,i.kt)("h2",{id:"example-recipe"},"Example Recipe"),(0,i.kt)("pre",null,(0,i.kt)("code",{parentName:"pre",className:"language-json"},'{\n  "type": "theurgy:distillation",\n  "distillation_time": 200,\n  "ingredient": {\n    "tag": "forge:stone"\n  },\n  "ingredient_count": 10,\n  "result": {\n    "count": 1,\n    "item": "theurgy:mercury_shard"\n  }\n}\n')))}m.isMDXComponent=!0}}]);