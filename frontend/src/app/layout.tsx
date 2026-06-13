import type { Metadata } from 'next'
import './globals.css'

export const metadata: Metadata = {
  title: 'Kakebo',
  description: 'Personal finance management',
}

export default function RootLayout({ children }: { children: React.ReactNode }) {
  return (
    <html lang="pt-BR" suppressHydrationWarning>
      <head>
        <script
          dangerouslySetInnerHTML={{
            __html: `try{var t=localStorage.getItem('kakebo:theme');if(t==='dark')document.documentElement.classList.add('dark');}catch(e){}`,
          }}
        />
      </head>
      <body>{children}</body>
    </html>
  )
}
