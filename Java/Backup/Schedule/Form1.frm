VERSION 5.00
Begin VB.Form Form1 
   Caption         =   "Schedule Program"
   ClientHeight    =   2100
   ClientLeft      =   60
   ClientTop       =   450
   ClientWidth     =   3810
   Icon            =   "Form1.frx":0000
   LinkTopic       =   "Form1"
   LockControls    =   -1  'True
   MaxButton       =   0   'False
   ScaleHeight     =   2100
   ScaleWidth      =   3810
   StartUpPosition =   2  'CenterScreen
   Begin VB.Frame Frame1 
      Height          =   1935
      Left            =   90
      TabIndex        =   0
      Top             =   30
      Width           =   3615
      Begin VB.Timer Calendar 
         Interval        =   1000
         Left            =   0
         Top             =   120
      End
      Begin VB.Label Label4 
         Caption         =   "Schedule Program"
         BeginProperty Font 
            Name            =   "MS Sans Serif"
            Size            =   9.75
            Charset         =   0
            Weight          =   700
            Underline       =   0   'False
            Italic          =   0   'False
            Strikethrough   =   0   'False
         EndProperty
         Height          =   255
         Left            =   840
         TabIndex        =   4
         Top             =   240
         Width           =   2055
      End
      Begin VB.Label Label3 
         Caption         =   "Stock Services'"
         Height          =   255
         Left            =   840
         TabIndex        =   3
         Top             =   1440
         Width           =   2175
      End
      Begin VB.Label Label2 
         Caption         =   "Stock Calendar"
         Height          =   375
         Left            =   360
         TabIndex        =   2
         Top             =   1080
         Width           =   3135
      End
      Begin VB.Label Label1 
         Caption         =   "Current Time"
         Height          =   375
         Left            =   600
         TabIndex        =   1
         Top             =   720
         Width           =   2775
      End
   End
   Begin VB.Menu mnuSystray 
      Caption         =   "Systray"
      Visible         =   0   'False
      Begin VB.Menu mnuShowHide 
         Caption         =   "&Show or Hide"
      End
      Begin VB.Menu mnuExit 
         Caption         =   "&Exit"
      End
   End
End
Attribute VB_Name = "Form1"
Attribute VB_GlobalNameSpace = False
Attribute VB_Creatable = False
Attribute VB_PredeclaredId = True
Attribute VB_Exposed = False
Private stockRunTime As Date
Private stockInterval As Integer
Private Type SYSTEMTIME
   wYear As Integer
   wMonth As Integer
   wDayOfWeek As Integer
   wDay As Integer
   wHour As Integer
   wMinute As Integer
   wSecond As Integer
   wMilliseconds As Integer
End Type

Private vcbRptRunTime As SYSTEMTIME
Private formState As Integer

Private Sub Cmdlog_Click()
    frmlog.Show vbModal
End Sub

Private Sub Form_Load()
    AddToTray Me, mnuSystray
    SetTrayIcon Me.Icon
    SetTrayTip "Schedule program"

    stockInterval = 15 '1 ngay
    stockRunTime = Now
    vcbRptRunTime.wHour = 8
    vcbRptRunTime.wMinute = 0
    vcbRptRunTime.wSecond = 0
    
    formState = 1
    'Me.Hide
    
    Calendar_Timer
End Sub

Private Sub Form_Resize()
    If Me.WindowState = vbMinimized Then
        formState = 0
        Me.Hide
    End If
End Sub

Private Sub Form_Unload(Cancel As Integer)
    RemoveFromTray
End Sub






Private Sub mnuExit_Click()
    Unload Me
End Sub

Private Sub mnuShowHide_Click()
    If formState = 1 Then
        formState = 0
        Me.Hide
    Else
        formState = 1
        Me.WindowState = 0
        Me.Show
    End If
End Sub

Private Sub Calendar_Timer()
    Dim dt As Date
    dt = Now
    Label1.Caption = "Current Time: " & Hour(dt) & ":" & Minute(dt) & ":" & Second(dt) & " " & Day(dt) & "-" & Month(dt) & "-" & Year(dt)
    If tcbRunTime <= dt Then
        TCBCalendarRun
    End If
   
End Sub

Private Sub TCBCalendarRun()
On Error GoTo ErrHandle
    Dim dt As Date
    
    'MsgBox (Hour(Now))
    
        Shell "G:\Services\Chungkhoan\classes\report1.bat", vbNormalNoFocus
        Shell "G:\Services\Chungkhoan\classes\report1.bat", vbNormalNoFocus
    'MsgBox "Stock calendar"
    tcbRunTime = DateAdd("n", stockInterval, stockRunTime)
    Label2.Caption = "Stock service run at: " & Hour(stockRunTime) & ":" & Minute(stockRunTime) & ":" & Second(stockRunTime) & " " & Day(stockRunTime) & "-" & Month(stockRunTime) & "-" & Year(stockRunTime)
    Exit Sub
ErrHandle:
End Sub


