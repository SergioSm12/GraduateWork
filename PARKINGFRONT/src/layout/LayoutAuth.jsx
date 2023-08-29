import React from 'react'
import { Outlet } from 'react-router-dom'

export const LayoutAuth = () => {
  return (
    <div className='min-h-screen flex items-center justify-center p-4'>
        <Outlet/>
    </div>
  )
}
