import React from 'react'
import { Route, Routes } from 'react-router-dom'
import { Users } from '../pages/admin/Users'
import { ShowUser } from '../components/Users/ShowUser'

export const UserRoutes = () => {
  return (
    <Routes>
        <Route index element={<Users />} />
        <Route path="/show/:id" element={<ShowUser />} />
    </Routes>
  )
}
