import React from "react";
import { render, screen } from '@testing-library/react';
import ProfileOverview from "./ProfileOverview";
import {MemoryRouter as Router} from 'react-router-dom';

describe('component test :: ProfileOverview', () => {

    const renderPage = () => {
        render(
            <Router>
                <ProfileOverview/>
            </Router>
        );
    }

    it('renders the menuappbar with the right header', () => {
        //Given
        renderPage();

        //When
        const appbar = screen.getByRole("heading", /Profiles/i)

        //Then
        expect(appbar).toBeInTheDocument();
    });
});