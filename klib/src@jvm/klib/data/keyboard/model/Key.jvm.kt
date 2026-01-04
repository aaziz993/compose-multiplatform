package klib.data.keyboard.model

import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent
import java.awt.event.KeyEvent as JavaKeyEvent

internal fun NativeKeyEvent.toKey(): Key = when (keyCode) {
    NativeKeyEvent.VC_ESCAPE -> Key.Esc
    NativeKeyEvent.VC_1 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number1
    NativeKeyEvent.VC_2 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number2
    NativeKeyEvent.VC_3 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number3
    NativeKeyEvent.VC_4 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number4
    NativeKeyEvent.VC_5 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number5
    NativeKeyEvent.VC_6 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number6
    NativeKeyEvent.VC_7 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number7
    NativeKeyEvent.VC_8 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number8
    NativeKeyEvent.VC_9 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number9
    NativeKeyEvent.VC_0 if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Number0
    NativeKeyEvent.VC_MINUS if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Minus
    NativeKeyEvent.VC_EQUALS if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Equal
    NativeKeyEvent.VC_BACKSPACE -> Key.Backspace
    NativeKeyEvent.VC_TAB -> Key.Tab
    NativeKeyEvent.VC_Q -> Key.Q
    NativeKeyEvent.VC_W -> Key.W
    NativeKeyEvent.VC_E -> Key.E
    NativeKeyEvent.VC_R -> Key.R
    NativeKeyEvent.VC_T -> Key.T
    NativeKeyEvent.VC_Y -> Key.Y
    NativeKeyEvent.VC_U -> Key.U
    NativeKeyEvent.VC_I -> Key.I
    NativeKeyEvent.VC_O -> Key.O
    NativeKeyEvent.VC_P -> Key.P
    NativeKeyEvent.VC_OPEN_BRACKET -> Key.LeftBrace
    NativeKeyEvent.VC_CLOSE_BRACKET -> Key.RightBrace
    NativeKeyEvent.VC_ENTER if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Enter
    NativeKeyEvent.VC_CONTROL if keyLocation == NativeKeyEvent.KEY_LOCATION_LEFT -> Key.LeftCtrl
    NativeKeyEvent.VC_A -> Key.A
    NativeKeyEvent.VC_S -> Key.S
    NativeKeyEvent.VC_D -> Key.D
    NativeKeyEvent.VC_F -> Key.F
    NativeKeyEvent.VC_G -> Key.G
    NativeKeyEvent.VC_H -> Key.H
    NativeKeyEvent.VC_J -> Key.J
    NativeKeyEvent.VC_K -> Key.K
    NativeKeyEvent.VC_L -> Key.L
    NativeKeyEvent.VC_SEMICOLON -> Key.Semicolon
    NativeKeyEvent.VC_QUOTE -> Key.Apostrophe
    NativeKeyEvent.VC_BACKQUOTE -> Key.Backtick
    NativeKeyEvent.VC_SHIFT if keyLocation == NativeKeyEvent.KEY_LOCATION_LEFT -> Key.LeftShift
    NativeKeyEvent.VC_BACK_SLASH -> Key.Backslash
    NativeKeyEvent.VC_Z -> Key.Z
    NativeKeyEvent.VC_X -> Key.X
    NativeKeyEvent.VC_C -> Key.C
    NativeKeyEvent.VC_V -> Key.V
    NativeKeyEvent.VC_B -> Key.B
    NativeKeyEvent.VC_N -> Key.N
    NativeKeyEvent.VC_M -> Key.M
    NativeKeyEvent.VC_COMMA if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Comma
    NativeKeyEvent.VC_PERIOD if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Dot
    NativeKeyEvent.VC_SLASH if keyLocation == NativeKeyEvent.KEY_LOCATION_STANDARD -> Key.Slash
    NativeKeyEvent.VC_SHIFT if keyLocation == NativeKeyEvent.KEY_LOCATION_RIGHT -> Key.RightShift
    NativeKeyEvent.VC_PRINTSCREEN if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadAsterisk
    NativeKeyEvent.VC_ALT if keyLocation == NativeKeyEvent.KEY_LOCATION_LEFT -> Key.LeftAlt
    NativeKeyEvent.VC_SPACE -> Key.Space
    NativeKeyEvent.VC_CAPS_LOCK -> Key.CapsLock
    NativeKeyEvent.VC_F1 -> Key.F1
    NativeKeyEvent.VC_F2 -> Key.F2
    NativeKeyEvent.VC_F3 -> Key.F3
    NativeKeyEvent.VC_F4 -> Key.F4
    NativeKeyEvent.VC_F5 -> Key.F5
    NativeKeyEvent.VC_F6 -> Key.F6
    NativeKeyEvent.VC_F7 -> Key.F7
    NativeKeyEvent.VC_F8 -> Key.F8
    NativeKeyEvent.VC_F9 -> Key.F9
    NativeKeyEvent.VC_F10 -> Key.F10
    NativeKeyEvent.VC_NUM_LOCK -> Key.NumLock
    NativeKeyEvent.VC_SCROLL_LOCK -> Key.ScrollLock
    NativeKeyEvent.VC_7 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad7
    NativeKeyEvent.VC_8 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad8
    NativeKeyEvent.VC_9 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad9
    NativeKeyEvent.VC_MINUS if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadMinus
    NativeKeyEvent.VC_4 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad4
    NativeKeyEvent.VC_5 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad5
    NativeKeyEvent.VC_6 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad6
    0x0E4E if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadPlus
    NativeKeyEvent.VC_1 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad1
    NativeKeyEvent.VC_2 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad2
    NativeKeyEvent.VC_3 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad3
    NativeKeyEvent.VC_0 if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.Keypad0
    NativeKeyEvent.VC_PERIOD if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadDot
    NativeKeyEvent.VC_F11 -> Key.F11
    NativeKeyEvent.VC_F12 -> Key.F12
    NativeKeyEvent.VC_ENTER if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadEnter
    NativeKeyEvent.VC_CONTROL if keyLocation == NativeKeyEvent.KEY_LOCATION_RIGHT -> Key.RightCtrl
    NativeKeyEvent.VC_SLASH if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadSlash
    NativeKeyEvent.VC_ALT if keyLocation == NativeKeyEvent.KEY_LOCATION_RIGHT -> Key.RightAlt
    NativeKeyEvent.VC_HOME -> Key.Home
    NativeKeyEvent.VC_UP -> Key.Up
    NativeKeyEvent.VC_PAGE_UP -> Key.PageUp
    NativeKeyEvent.VC_LEFT -> Key.Left
    NativeKeyEvent.VC_RIGHT -> Key.Right
    NativeKeyEvent.VC_END -> Key.End
    NativeKeyEvent.VC_DOWN -> Key.Down
    NativeKeyEvent.VC_PAGE_DOWN -> Key.PageDown
    NativeKeyEvent.VC_INSERT -> Key.Insert
    NativeKeyEvent.VC_DELETE -> Key.Delete
    NativeKeyEvent.VC_VOLUME_MUTE -> Key.Mute
    NativeKeyEvent.VC_VOLUME_DOWN -> Key.VolumeDown
    NativeKeyEvent.VC_VOLUME_UP -> Key.VolumeUp
    NativeKeyEvent.VC_POWER -> Key.Power
    NativeKeyEvent.VC_EQUALS if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadEqual
    NativeKeyEvent.VC_PAUSE -> Key.Pause
    NativeKeyEvent.VC_COMMA if keyLocation == NativeKeyEvent.KEY_LOCATION_NUMPAD -> Key.KeypadComma
    NativeKeyEvent.VC_META if keyLocation == NativeKeyEvent.KEY_LOCATION_LEFT -> Key.LeftSuper
    NativeKeyEvent.VC_META if keyLocation == NativeKeyEvent.KEY_LOCATION_RIGHT -> Key.RightSuper
//   NativeKeyEvent.Compose  -> Key.Compose
    NativeKeyEvent.VC_SUN_STOP -> Key.Stop
    NativeKeyEvent.VC_SUN_AGAIN -> Key.Again
    NativeKeyEvent.VC_SUN_PROPS -> Key.Props
    NativeKeyEvent.VC_SUN_UNDO -> Key.Undo
    NativeKeyEvent.VC_SUN_FRONT -> Key.Front
    NativeKeyEvent.VC_SUN_COPY -> Key.Copy
    NativeKeyEvent.VC_SUN_OPEN -> Key.Open
//  NativeKeyEvent.Paste   -> Key.Paste
    NativeKeyEvent.VC_SUN_FIND -> Key.Find
    NativeKeyEvent.VC_SUN_CUT -> Key.Cut
    NativeKeyEvent.VC_SUN_HELP -> Key.Help
    NativeKeyEvent.VC_CONTEXT_MENU -> Key.Menu
    NativeKeyEvent.VC_APP_CALCULATOR -> Key.Calc
// NativeKeyEvent.Setup    -> Key.Setup
    NativeKeyEvent.VC_SLEEP -> Key.Sleep
    NativeKeyEvent.VC_WAKE -> Key.Wakeup
    NativeKeyEvent.VC_APP_MAIL -> Key.Mail
    NativeKeyEvent.VC_BROWSER_FAVORITES -> Key.Bookmarks
//  NativeKeyEvent.Computer   -> Key.Computer
    NativeKeyEvent.VC_BROWSER_BACK -> Key.Back
    NativeKeyEvent.VC_BROWSER_FORWARD -> Key.Forward
    NativeKeyEvent.VC_MEDIA_NEXT -> Key.NextSong
    NativeKeyEvent.VC_MEDIA_PLAY -> Key.PlayPause
    NativeKeyEvent.VC_MEDIA_PREVIOUS -> Key.PreviousSong
    NativeKeyEvent.VC_MEDIA_STOP -> Key.StopCd
    0 if rawCode == 176 -> Key.Record
//    NativeKeyEvent.Rewind -> Key.Rewind
//   NativeKeyEvent.Phone  -> Key.Phone
    NativeKeyEvent.VC_BROWSER_REFRESH -> Key.Refresh
    NativeKeyEvent.VC_F13 -> Key.F13
    NativeKeyEvent.VC_F14 -> Key.F14
    NativeKeyEvent.VC_F15 -> Key.F15
    NativeKeyEvent.VC_F16 -> Key.F16
    NativeKeyEvent.VC_F17 -> Key.F17
    NativeKeyEvent.VC_F18 -> Key.F18
    NativeKeyEvent.VC_F19 -> Key.F19
    NativeKeyEvent.VC_F20 -> Key.F20
    NativeKeyEvent.VC_F21 -> Key.F21
    NativeKeyEvent.VC_F22 -> Key.F22
    NativeKeyEvent.VC_F23 -> Key.F23
    NativeKeyEvent.VC_F24 -> Key.F24
    else -> Key.Unknown
}

internal val KEYS: Map<Key, Int> = mapOf(
    Key.Esc to JavaKeyEvent.VK_ESCAPE,
    Key.Number1 to JavaKeyEvent.VK_1,
    Key.Number2 to JavaKeyEvent.VK_2,
    Key.Number3 to JavaKeyEvent.VK_3,
    Key.Number4 to JavaKeyEvent.VK_4,
    Key.Number5 to JavaKeyEvent.VK_5,
    Key.Number6 to JavaKeyEvent.VK_6,
    Key.Number7 to JavaKeyEvent.VK_7,
    Key.Number8 to JavaKeyEvent.VK_8,
    Key.Number9 to JavaKeyEvent.VK_9,
    Key.Number0 to JavaKeyEvent.VK_0,
    Key.Minus to JavaKeyEvent.VK_MINUS,
    Key.Equal to JavaKeyEvent.VK_EQUALS,
    Key.Backspace to JavaKeyEvent.VK_BACK_SPACE,
    Key.Tab to JavaKeyEvent.VK_TAB,
    Key.Q to JavaKeyEvent.VK_Q,
    Key.W to JavaKeyEvent.VK_W,
    Key.E to JavaKeyEvent.VK_E,
    Key.R to JavaKeyEvent.VK_R,
    Key.T to JavaKeyEvent.VK_T,
    Key.Y to JavaKeyEvent.VK_Y,
    Key.U to JavaKeyEvent.VK_U,
    Key.I to JavaKeyEvent.VK_I,
    Key.O to JavaKeyEvent.VK_O,
    Key.P to JavaKeyEvent.VK_P,
    Key.LeftBrace to JavaKeyEvent.VK_OPEN_BRACKET,
    Key.RightBrace to JavaKeyEvent.VK_CLOSE_BRACKET,
    Key.Enter to JavaKeyEvent.VK_ENTER,
    Key.LeftCtrl to JavaKeyEvent.VK_CONTROL,
    Key.A to JavaKeyEvent.VK_A,
    Key.S to JavaKeyEvent.VK_S,
    Key.D to JavaKeyEvent.VK_D,
    Key.F to JavaKeyEvent.VK_F,
    Key.G to JavaKeyEvent.VK_G,
    Key.H to JavaKeyEvent.VK_H,
    Key.J to JavaKeyEvent.VK_J,
    Key.K to JavaKeyEvent.VK_K,
    Key.L to JavaKeyEvent.VK_L,
    Key.Semicolon to JavaKeyEvent.VK_SEMICOLON,
    Key.Apostrophe to JavaKeyEvent.VK_QUOTE,
    Key.Backtick to JavaKeyEvent.VK_BACK_QUOTE,
    Key.LeftShift to JavaKeyEvent.VK_SHIFT,
    Key.Backslash to JavaKeyEvent.VK_BACK_SLASH,
    Key.Z to JavaKeyEvent.VK_Z,
    Key.X to JavaKeyEvent.VK_X,
    Key.C to JavaKeyEvent.VK_C,
    Key.V to JavaKeyEvent.VK_V,
    Key.B to JavaKeyEvent.VK_B,
    Key.N to JavaKeyEvent.VK_N,
    Key.M to JavaKeyEvent.VK_M,
    Key.Comma to JavaKeyEvent.VK_COMMA,
    Key.Dot to JavaKeyEvent.VK_PERIOD,
    Key.Slash to JavaKeyEvent.VK_SLASH,
    Key.RightShift to JavaKeyEvent.VK_SHIFT,
    Key.KeypadAsterisk to JavaKeyEvent.VK_MULTIPLY,
    Key.LeftAlt to JavaKeyEvent.VK_ALT,
    Key.Space to JavaKeyEvent.VK_SPACE,
    Key.CapsLock to JavaKeyEvent.VK_CAPS_LOCK,
    Key.F1 to JavaKeyEvent.VK_F1,
    Key.F2 to JavaKeyEvent.VK_F2,
    Key.F3 to JavaKeyEvent.VK_F3,
    Key.F4 to JavaKeyEvent.VK_F4,
    Key.F5 to JavaKeyEvent.VK_F5,
    Key.F6 to JavaKeyEvent.VK_F6,
    Key.F7 to JavaKeyEvent.VK_F7,
    Key.F8 to JavaKeyEvent.VK_F8,
    Key.F9 to JavaKeyEvent.VK_F9,
    Key.F10 to JavaKeyEvent.VK_F10,
    Key.NumLock to JavaKeyEvent.VK_NUM_LOCK,
    Key.ScrollLock to JavaKeyEvent.VK_SCROLL_LOCK,
    Key.Keypad7 to JavaKeyEvent.VK_NUMPAD7,
    Key.Keypad8 to JavaKeyEvent.VK_NUMPAD8,
    Key.Keypad9 to JavaKeyEvent.VK_NUMPAD9,
    Key.KeypadMinus to JavaKeyEvent.VK_SUBTRACT,
    Key.Keypad4 to JavaKeyEvent.VK_NUMPAD4,
    Key.Keypad5 to JavaKeyEvent.VK_NUMPAD5,
    Key.Keypad6 to JavaKeyEvent.VK_NUMPAD6,
    Key.KeypadPlus to JavaKeyEvent.VK_ADD,
    Key.Keypad1 to JavaKeyEvent.VK_NUMPAD1,
    Key.Keypad2 to JavaKeyEvent.VK_NUMPAD2,
    Key.Keypad3 to JavaKeyEvent.VK_NUMPAD3,
    Key.Keypad0 to JavaKeyEvent.VK_NUMPAD0,
    Key.KeypadDot to JavaKeyEvent.VK_DECIMAL,
    Key.F11 to JavaKeyEvent.VK_F11,
    Key.F12 to JavaKeyEvent.VK_F12,
    Key.KeypadEnter to JavaKeyEvent.VK_ENTER,
    Key.RightCtrl to JavaKeyEvent.VK_CONTROL,
    Key.KeypadSlash to JavaKeyEvent.VK_DIVIDE,
    Key.RightAlt to JavaKeyEvent.VK_ALT,
    Key.Home to JavaKeyEvent.VK_HOME,
    Key.Up to JavaKeyEvent.VK_UP,
    Key.PageUp to JavaKeyEvent.VK_PAGE_UP,
    Key.Left to JavaKeyEvent.VK_LEFT,
    Key.Right to JavaKeyEvent.VK_RIGHT,
    Key.End to JavaKeyEvent.VK_END,
    Key.Down to JavaKeyEvent.VK_DOWN,
    Key.PageDown to JavaKeyEvent.VK_PAGE_DOWN,
    Key.Insert to JavaKeyEvent.VK_INSERT,
    Key.Delete to JavaKeyEvent.VK_DELETE,
//    Key.Mute to JavaKeyEvent.Mute,
//    Key.VolumeDown to JavaKeyEvent.VolumeDown,
//    Key.VolumeUp to JavaKeyEvent.VolumeUp,
//    Key.Power to JavaKeyEvent.Power,
    Key.KeypadEqual to JavaKeyEvent.VK_EQUALS,
    Key.Pause to JavaKeyEvent.VK_PAUSE,
//    Key.KeypadComma to JavaKeyEvent.KeypadComma,
    Key.LeftSuper to JavaKeyEvent.VK_META,
    Key.RightSuper to JavaKeyEvent.VK_META,
    Key.Compose to JavaKeyEvent.VK_COMPOSE,
    Key.Stop to JavaKeyEvent.VK_STOP,
    Key.Again to JavaKeyEvent.VK_AGAIN,
    Key.Props to JavaKeyEvent.VK_PROPS,
    Key.Undo to JavaKeyEvent.VK_UNDO,
//    Key.Front to JavaKeyEvent.Front,
    Key.Copy to JavaKeyEvent.VK_COPY,
//    Key.Open to JavaKeyEvent.Open,
    Key.Paste to JavaKeyEvent.VK_PASTE,
    Key.Find to JavaKeyEvent.VK_FIND,
    Key.Cut to JavaKeyEvent.VK_CUT,
    Key.Help to JavaKeyEvent.VK_HELP,
    Key.Menu to JavaKeyEvent.VK_CONTEXT_MENU,
//    Key.Calc to JavaKeyEvent.Calc,
//    Key.Setup to JavaKeyEvent.Setup,
//    Key.Sleep to JavaKeyEvent.Sleep,
//    Key.Wakeup to JavaKeyEvent.Wakeup,
//    Key.Mail to JavaKeyEvent.Mail,
//    Key.Bookmarks to JavaKeyEvent.Bookmarks,
//    Key.Computer to JavaKeyEvent.Computer,
//    Key.Back to JavaKeyEvent.Back,
//    Key.Forward to JavaKeyEvent.Forward,
//    Key.NextSong to JavaKeyEvent.NextSong,
//    Key.PlayPause to JavaKeyEvent.PlayPause,
//    Key.PreviousSong to JavaKeyEvent.PreviousSong,
//    Key.StopCd to JavaKeyEvent.StopCd,
//    Key.Record to JavaKeyEvent.Record,
//    Key.Rewind to JavaKeyEvent.Rewind,
//    Key.Phone to JavaKeyEvent.Phone,
//    Key.Refresh to JavaKeyEvent.Refresh,
    Key.F13 to JavaKeyEvent.VK_F13,
    Key.F14 to JavaKeyEvent.VK_F14,
    Key.F15 to JavaKeyEvent.VK_F15,
    Key.F16 to JavaKeyEvent.VK_F16,
    Key.F17 to JavaKeyEvent.VK_F17,
    Key.F18 to JavaKeyEvent.VK_F18,
    Key.F19 to JavaKeyEvent.VK_F19,
    Key.F20 to JavaKeyEvent.VK_F20,
    Key.F21 to JavaKeyEvent.VK_F21,
    Key.F22 to JavaKeyEvent.VK_F22,
    Key.F23 to JavaKeyEvent.VK_F23,
    Key.F24 to JavaKeyEvent.VK_F24,
)
