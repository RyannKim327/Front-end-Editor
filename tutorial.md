### Welcome to Front-end Editor
#### Tutorial Section

---
#### Table of Contents
- [Showing Edit field](#field)
- [Where are the menus](#menu)
- [Where is the javascript console](#console)
- [Where do my projects saved](#proSave)
- [How to create a color scheme for Front-end Editor](#color)
- [How to apply my customized color scheme](#apply)
- [How to apply a custom font for Front-end Editor](#font)
---
<div id="field"></div>

### Showing Edit Field
> To show the edit field, you've must swipe or click the back navigation of your phone. and activate Front-end Editor's file access permission to continue.

---
<div id="menu"></div>

### Where are the menus
> To show up the menu, just click or swipe up your back navigation, while the editing field is visible. A popup dialog will show up, and click the navigation. The other menu is when you clicked the gray line separator between the editing field and the webview output.

---
<div id="console"></div>

### Where is the javascript console
> The javascript console is just a popup dialog, it only shows when you click and hold the gray line separator between the editor and the output.

---
<div id="proSave"></div>

### Where do my projects save
> Majority, your projects were saved from the phone storage of your device, but if you set your SD Card as primary storage, then it will be saved there.

---
<div id="color"></div>

### How to create a color scheme for Front-end Editor
> I make it more simple and easy, you can create a color scheme using JSON as format. These are the things must have in your json file.

| name | data type | description |
|-------|-----------|------------|
| creator | String | This is to give credit to the creator of the color scheme. |
| appVersion | String | This is to avoid conflicts from other updates of the application if possible. |
| background | String (hex) | A hexadecimal format color code for background of the editing field. |
| textColor | String (hex) | A hexadecimal format color code for the base text color of the editing field. |
| syntax | Object | A set of syntax. |

Additional under syntax

| syntax name (key) | description |
|--------------|------------|
| html | For HTML color scheme |
| css | For CSS color scheme |
| javascript | For JavaScript color scheme |

Here are the additional keys under the three syntax name (key)

| key | datatype | description |
|-----|----------|------------|
| tag | String (hex) | A hexadecimal format color code for tags. |
| params | String (hex) | A hexadecimal format color code for parameters and arguments |
| string | String (hex) | A hexadecimal format color code for string, digits. |
| comment | String (hex) | A hexadecimal format color code for comments. |

The colors are all in RGB format. (Red, Green, Blue)

Here is the sample JSON Configuration, to make it clear.
```JSON
{
	"creator": "RySes",
	"appVersion": "AlphaLite 0.6 Beta 0.1",
	"background": "#333333",
	"textColor": "#ffffff",
	"syntax": {
		"html": {
			"tag": "#ff8370",
			"params": "#ff8ff2",
			"string": "#f0ff80",
			"comment": "#505050"
		},
		"css": {
			"tag": "#ff8370",
			"params": "#ff8ff2",
			"string": "#f0ff80",
			"comment": "#78ff81"
		},
		"javascript": {
			"tag": "#ff8370",
			"params": "#ff8ff2",
			"string": "#f0ff80",
			"comment": "#78ff81"
		}
	}
}
```

---
<div id="apply"></div>

### How to apply my customized color scheme
> Just simply go to the app's settings, and paste the full directory path of the json file. You need to include the file name, also the file extension.

---
<div id="font"></div>

### How to apply a custom font for Front-end Editor
> Its just same like on how you applying the color scheme on the app.
